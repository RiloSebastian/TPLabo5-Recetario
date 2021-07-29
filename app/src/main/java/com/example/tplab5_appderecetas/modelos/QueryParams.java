package com.example.tplab5_appderecetas.modelos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryParams {
    public String number;
    public String apiKey;
    public String query;
    public String includeIngredients;
    public String sort;
    public String ingredients;
    public String ranking;
    public String addRecipeInformation;
    public String fillIngredients;
    public String instructionsRequired;

    public QueryParams(){}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIncludeIngredients() {
        return includeIngredients;
    }

    public void setIncludeIngredients(String includeIngredients) {
        this.includeIngredients = includeIngredients;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getAddRecipeInformation() {
        return addRecipeInformation;
    }

    public void setAddRecipeInformation(String addRecipeInformation) {
        this.addRecipeInformation = addRecipeInformation;
    }

    public String getFillIngredients() {
        return fillIngredients;
    }

    public void setFillIngredients(String fillIngredients) {
        this.fillIngredients = fillIngredients;
    }

    public String getInstructionsRequired() {
        return instructionsRequired;
    }

    public void setInstructionsRequired(String instructionsRequired) {
        this.instructionsRequired = instructionsRequired;
    }

    public void clear(){
        this.number = null;
        this.apiKey = null;
        this.query = null;
        this.includeIngredients = null;
        this.sort = null;
        this.ingredients = null;
        this.ranking = null;
        this.addRecipeInformation = null;
        this.fillIngredients = null;
        this.instructionsRequired = null;
    }
}
