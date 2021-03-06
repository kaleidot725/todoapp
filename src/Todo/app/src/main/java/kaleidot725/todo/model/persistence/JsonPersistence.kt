package kaleidot725.todo.model.persistence

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kaleidot725.todo.model.entity.Task
import java.io.File
import java.lang.reflect.ParameterizedType

class JsonPersistence<T>(name : String, clazz : Class<T>) : Persistence<T> {
    val name: String = name

    private val moshi: Moshi = Moshi.Builder().build()
    private val type: ParameterizedType = Types.newParameterizedType(List::class.java, clazz)
    private val adapter: JsonAdapter<List<T>> = moshi.adapter(type)

    override fun save(list: List<T>) {
        try {
            val json = adapter.toJson(list)
            val file = File(name)
            file.absoluteFile.bufferedWriter().use { it.write(json) }
        } catch (e: Exception) {
            Log.d(this.javaClass.name.toString(), e.toString())
            throw e
        }
    }

    override fun load(): List<T> {
        try {
            val file = File(name)
            if (file.exists()) {
                val contents = file.absoluteFile.bufferedReader().use { it.readText() }
                return adapter.fromJson(contents) ?: arrayListOf()
            }
            return arrayListOf()
        } catch (e: Exception) {
            Log.d(this.javaClass.name.toString(), e.toString())
            throw e
        }
    }
}