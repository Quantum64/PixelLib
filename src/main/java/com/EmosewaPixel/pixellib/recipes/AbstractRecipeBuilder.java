package com.EmosewaPixel.pixellib.recipes;

import com.EmosewaPixel.pixellib.PixelLib;
import com.EmosewaPixel.pixellib.miscUtils.StreamUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Recipe Builders are builders used for easily creating Machine Recipes
public abstract class AbstractRecipeBuilder<T extends SimpleMachineRecipe, R extends AbstractRecipeBuilder<T, R>> {
    private AbstractRecipeList<T, R> recipeList;
    private List<Object> inputs = new ArrayList<>();
    private List<Float> consumeChances = new ArrayList<>();
    private List<Object> outputs = new ArrayList<>();
    private List<Float> outputChances = new ArrayList<>();
    private int time = 0;

    public AbstractRecipeBuilder(AbstractRecipeList<T, R> recipeList) {
        this.recipeList = recipeList;
    }

    protected List<Object> getInputs() {
        return inputs;
    }

    protected List<Object> getOutputs() {
        return outputs;
    }

    protected List<Float> getConsumeChances() {
        return consumeChances;
    }

    protected List<Float> getOutputChances() {
        return outputChances;
    }

    protected int getTime() {
        return time;
    }

    protected AbstractRecipeList<T, R> getRecipeList() {
        return recipeList;
    }

    public R input(Object... inputs) {
        this.inputs.addAll(Arrays.asList(inputs));
        StreamUtils.repeat(inputs.length, i -> consumeChances.add(1f));
        return (R) this;
    }

    public R potentiallyConsumable(Object input, float consumeChance) {
        this.inputs.add(input);
        consumeChances.add(consumeChance);
        return (R) this;
    }

    public R notConsumable(Object... inputs) {
        this.inputs.addAll(Arrays.asList(inputs));
        StreamUtils.repeat(inputs.length, i -> consumeChances.add(0f));
        return (R) this;
    }

    public R output(Object... outputs) {
        this.outputs.addAll(Arrays.asList((outputs)));
        StreamUtils.repeat(outputs.length, i -> outputChances.add(1.0f));
        return (R) this;
    }

    public R chancedOutput(Object output, float chance) {
        this.outputs.add(output);
        outputChances.add(chance);
        return (R) this;
    }

    public R time(int amount) {
        time = amount;
        return (R) this;
    }

    public abstract T build();

    public void buildAndRegister() {
        if (!build().isEmpty())
            recipeList.add(build());
        else
            PixelLib.LOGGER.error("Recipe with output {} is empty", outputs);
    }
}
