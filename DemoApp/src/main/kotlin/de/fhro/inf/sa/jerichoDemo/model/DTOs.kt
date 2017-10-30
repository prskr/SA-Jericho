package de.fhro.inf.sa.jerichoDemo.model

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * @author Peter Kurfer
 * Created on 10/29/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
data class JokeDto(var id: Int = 0, var joke: String = "", var category: String? = "")

@JsonInclude(JsonInclude.Include.NON_NULL)
data class JokesArrayDto(var count: Int = 0, var jokes: List<JokeDto> = listOf())

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoriesArrayDto(var count: Int = 0, var categories: List<CategoryDto> = listOf())

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryDto(var id: Int = 0, var name: String = "")