package com.EmosewaPixel.pixellib.recipes;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//Simple Machine Recipes are the most basic Machine Recipes, only having inputs, outputs and a processing time
public class SimpleMachineRecipe {
    private Object[] input;
    private Object[] output;
    private Float[] consumeChances;
    private Float[] outputChances;
    private int time;
    public static final SimpleMachineRecipe EMPTY = new SimpleMachineRecipe(new Object[0], new Float[0], new Object[0], new Float[0], 0);

    public SimpleMachineRecipe(Object[] input, Float[] consumeChances, Object[] output, Float[] outputChances, int time) {
        this.input = input;
        this.output = output;
        this.time = time;
        this.consumeChances = consumeChances;
        this.outputChances = outputChances;
    }

    public Object getInput(int index) {
        return input[index];
    }

    public int getInputCount(int index) {
        Object obj = getInput(index);
        if (obj instanceof ItemStack)
            return ((ItemStack) obj).getCount();
        if (obj instanceof TagStack)
            return ((TagStack) obj).getCount();
        return 0;
    }

    public Object[] getAllInputs() {
        return input;
    }

    public ItemStack getOutput(int index) {
        if (output[index] instanceof ItemStack)
            return (ItemStack) output[index];
        else if (output[index] instanceof TagStack)
            return ((TagStack) output[index]).asItemStack();
        return null;
    }

    public ItemStack[] getAllOutputs() {
        return (ItemStack[]) IntStream.range(0, output.length)
                .mapToObj(i -> output[i] instanceof ItemStack ? output[i] : ((TagStack) output[i]).asItemStack()).toArray();
    }

    public Float getConsumeChance(int index) {
        return consumeChances[index];
    }


    public Float getOutputChance(int index) {
        return outputChances[index];
    }

    public Float[] getOutputChances() {
        return outputChances;
    }

    public List<List<ItemStack>> getInputsAsList() {
        return Arrays.stream(input).map(o -> {
            if (o instanceof ItemStack)
                return ImmutableList.of((ItemStack) o);
            TagStack stack = (TagStack) o;
            return stack.geTag().getAllElements().stream().map(i -> new ItemStack(i, stack.getCount())).collect(Collectors.toList());
        }).collect(Collectors.toList());
    }

    public List<ItemStack> getOutputsAsList() {
        return Arrays.asList(getAllOutputs());
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isInputValid(ItemStack[] stacks) {
        if (stacks.length != input.length || Arrays.stream(stacks).anyMatch(ItemStack::isEmpty) ||
                Arrays.stream(input).anyMatch(s -> s instanceof ItemStack ? ((ItemStack) s).isEmpty() : ((TagStack) s).isEmpty()))
            return false;

        return stacks.length == Arrays.stream(input).map(stackRec -> Arrays.stream(stacks)
                .filter(stack -> stackRec instanceof ItemStack ? (stack.isItemEqual((ItemStack) stackRec) && stack.getCount() >= ((ItemStack) stackRec).getCount()) : ((TagStack) stackRec).geTag().contains(stack.getItem()) && stack.getCount() >= ((TagStack) stackRec).getCount())
                .findFirst().orElse(ItemStack.EMPTY))
                .filter(o -> o != ItemStack.EMPTY)
                .count();
    }

    public int getCountOfInput(int index) {
        Object o = input[index];
        return o instanceof ItemStack ? ((ItemStack) o).getCount() : ((TagStack) o).getCount();
    }

    public int getIndexOfInput(ItemStack stack) {
        Object o = Arrays.stream(input).filter(input -> input instanceof ItemStack ? stack.isItemEqual((ItemStack) input) : ((TagStack) input).geTag().contains(stack.getItem())).findFirst().get();
        if (o == null)
            return -1;
        return Arrays.asList(input).indexOf(o);
    }

    public boolean itemBelongsInRecipe(ItemStack stack) {
        if (stack.isEmpty() || input == null)
            return false;

        return Arrays.stream(input).anyMatch(stackRec -> {
            if (stackRec instanceof ItemStack) {
                return ((ItemStack) stackRec).isItemEqual(stack);
            } else
                return ((TagStack) stackRec).geTag().contains(stack.getItem());
        });
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
