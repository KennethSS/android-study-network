package com.study.network.http.converter

import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

class JsonConverter {

    fun convertToObject(json: JSONObject, c: Class<*>, newInstance: Any? = null): Any {
        val instance = newInstance ?: c.newInstance()

        c.declaredFields.forEach { field ->
            field.isAccessible = true

            when(field.type) {
                String::class.java -> field.set(instance, json.getString(field.name))
                CharSequence::class.java -> field.set(instance, json.getString(field.name))
                Integer::class.java -> field.set(instance, json.getInt(field.name))
                Long::class.java -> field.set(instance, json.getLong(field.name))
                Float::class.java -> field.set(instance, json.getDouble(field.name))
                Double::class.java -> field.set(instance, json.getDouble(field.name))
                java.lang.Boolean::class.java -> {
                    println("fieldName: ${field.name}")
                    field.set(instance, json.getBoolean(field.name))
                }
                Boolean::class.java -> field.set(instance, json.getBoolean(field.name))
                List::class.java -> convertToArray(json, field, instance)
                ArrayList::class.java -> convertToArray(json, field, instance)
                else -> field.set(instance, convertCustomType(json, field))
            }
        }

        return instance
    }

    private fun convertCustomType(jsonObject: JSONObject, field: Field): Any {

        val obj = jsonObject.getJSONObject(field.name)
        println("customtype ${obj.toString()}")
        println("customtype ${field.name}")
        return convertToObject(obj, field.type)
    }

    private fun convertToArray(jsonObject: JSONObject, field: Field, instance: Any) {
        val jsonArray = jsonObject.getJSONArray(field.name)

        if (jsonArray.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val genericType = field.genericType

                if (genericType is ParameterizedType) {
                    val result = genericType.actualTypeArguments.map { type ->
                        convertToObject(jsonArray.getJSONObject(i), type as Class<*>)
                    }

                    field.set(instance, result)
                }
            }
        }
    }

    /*fun <T>jsonArrayToInstance(jsonArray: JSONArray, cls: Class<*>): T {
        val component = cls.componentType

        val array = arrayListOf<Any>()

        if (jsonArray.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val d = convertToObject(jsonArray.getJSONObject(i), component)
                println("5: $d")
                array.add(d)
            }
        }

        return array.toArray()
    }*/
}