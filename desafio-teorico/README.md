# Consignas

## Procesos, hilos y corrutinas

- Proceso: Un sistema de edición de vídeo

~~~
- Si una parte de la renderización falla, no afectará a otros trabajos en curso.
- Cada trabajo de renderización puede aprovechar al máximo los recursos del sistema.
- Diferentes trabajos de renderización pueden ejecutarse con diferentes configuraciones.
~~~

- Hilos: Servidor web que atiende múltipes solicitudes simultáneas

~~~
- Los hilos pueden compartir la misma caché de datos y de conexiones a base de datos.
- Crear un hilo es más rápido que crear un proceso, por lo que puede antender nuevas 
    solicitudes rápidamente. 
- Los hilos permiten un buen aprovechamiento de sistemas multi núcleo.
~~~

- Corrutinas: Aplicación de chat en tiempo real

~~~
- Permite manejar muchas conexiones con un número limitado de hilos.
- Ideal para operaciones que implican mucha espera.
- Permite manejar un gran número de conexiones simúltaneas sin necesidad de aumentar 
    (mucho) los recursos del servidor.
~~~

## Optimización de recursos del sistema operativo

~~~
Para una cantidad de procesamiento así de alta, el enfoque debe ser la eficiencia y el 
uso adecuado de los recursos del sistema.
Debería tenerse en cuenta:

- Arquitectura general: Diseño asíncrono y no bloqueante.
- Tecnologías: Orientadas al uso de corrutinas o programación reactiva.
- Estrategias:
-- Procesamiento por lotes: Dividir el total de elementos en lotes más pequeños.
-- Paralelismo: Procesar lotes concurrentemente, pero limitando el número de lotes
    simultáneos.
-- Backpressure: Implementar mecánismos de backpressure para controlar la velocidad de
    procesamiento y evitar el desbordamiento de memoria.
-- Caché: Utilizar una caché para almacenar resultados frecuentes.
-- Reintentos y Circuit breaker: Para manejar fallos temporales en la API.
-- Conexiones HTTP persistentes: Para reducir el overhead al establecer nuevas conexiones.
-- Compresión: Para reducir el ancho de banda utilizado.

Fuera de lo anterior, debe tenerse un sistema de monitoreo para verificar el rendimiento
y así ajustar parámetros como el tamaño del lote y el número de lotes concurrentes.

También podría distribuirse el procesamiento escalando horizontalmente (más de una instancia)

También verificaría si a nivel de sistema operativo, puede ampliarse el número de conexiones
TCP para incrementar el volumen de trabajo.
~~~

## Análisis de complejidad

- Algoritmos O(n2), O(n3), O(2n) y O(n log n)

~~~
En un contexto en el que se manejan grandes cantidades de elementos, favorecería el algoritmo
O(n log n) dado que escala mejor para conjuntos de datos grandes que 0(n2), por ejemplo.

Descartaría 0(2n) dado que por cada elemento el tiempo de ejecución se duplica.
Para un conjunto de datos grande, se vuelve algo inviable incluso si se cuenta con hardware 
potente.
~~~

- Para la base de datos AlfaDB con una complejidad de O(1) en consulta y O(n2) en escritura

~~~
Dado para este caso, en que la lectura es frecuente pero la escritura no, la usaría para caché de
datos, tablas de búsqueda o sistemas de autenticación.
~~~

- Para la base de datos BetaDB que tiene una complejidad de O(log n) tanto para consulta, como para escritura

~~~
Dado este caso, en que la lectura y escritura tienen una complejidad igual, la usaría para bases de
datos transaccionales, sistemas de gestión de inventario, o sistemas de registro balanceados.
~~~