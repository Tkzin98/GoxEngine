package org.godotengine.editor.scripting.java

import android.util.Log
import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader
import org.codehaus.janino.SimpleCompiler

/**
 * Carrega e executa código Java dinamicamente usando Janino.
 */
object JavaScriptEngine {
    private var compiler: SimpleCompiler? = null
    private var runMethod: Method? = null
    private var compiledClass: Class<*>? = null

    init {
        try {
            // Caminho do jar Janino correto
            val jarPath = "/data/data/org.godotengine.editor.v4/lib/libs/janino-3.1.6.jar"
            val jarFile = File(jarPath)

            if (jarFile.exists()) {
                val loader = URLClassLoader(arrayOf(jarFile.toURI().toURL()), ClassLoader.getSystemClassLoader())
                compiler = SimpleCompiler()
                compiler!!.setParentClassLoader(loader)
                Log.i("JavaEngine", "Janino carregado com sucesso!")
            } else {
                Log.e("JavaEngine", "Arquivo .jar não encontrado em: $jarPath")
            }
        } catch (e: Exception) {
            Log.e("JavaEngine", "Erro ao inicializar Janino: ${e.message}")
        }
    }

    /**
     * Compila e executa um código Java simples.
     * @param code Código Java com uma classe pública chamada TestRunner e método estático run()
     */
    fun run(code: String): String {
        return try {
            if (compiler == null) return "Engine Java não inicializada!"

            // Compila o código
            compiler!!.cook(code)
            compiledClass = compiler!!.classLoader.loadClass("TestRunner") // Nome da classe que você passa no código
            runMethod = compiledClass!!.getMethod("run")

            // Executa o método run()
            val result = runMethod!!.invoke(null)
            "Executado com sucesso: $result"
        } catch (e: Exception) {
            "Erro executando código Java: ${e.message}"
        }
    }
}