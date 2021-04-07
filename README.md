# Buscador Mercado Libre

El propósito de este documento es explicar las principales funcionalidades y arquitectura utilizada en la aplicación.

## FUNCIONAMIENTO
La aplicación consiste en un buscador que utiliza la API de Mercado Libre, el mismo muestra productos sugeridos antes de realizar la búsqueda, permite al usuario buscar productos y visualizar los detalles de cada uno de ellos. Para esto cuenta con dos pantallas principales, la primera para realizar la búsqueda y mostrar los resultados y la segunda para mostrar los detalles del producto seleccionado.

### Pantalla de búsqueda

Esta pantalla es la primera en mostrarse cuando el usuario inicia la aplicación, contiene dos secciones fundamentales, el componente de búsqueda y el listado donde se muestran tanto las recomendaciones como los resultados de la búsqueda.
Inicialmente, teniendo en cuenta que el usuario no ha realizado búsqueda alguna, se muestran recomendaciones basadas en las búsquedas más realizadas en esa región, esta funcionalidad utiliza datos simulados, los cuales son fijos y arbitrarios dado que no es un producto real.
Las recomendaciones se cargan en páginas de 15 resultados y se agregarán más resultados con un criterio aleatorio cada vez que el usuario llegue al fondo de la lista de forma automática.
Una vez el usuario realice una búsqueda, los resultados de la misma se mostrarán debajo del componente de búsqueda en bloques de 50 resultados. El usuario podrá cargar más resultados desplazándose hasta el fondo de la lista y en ese mometo se mostrará un indicador de que se están cargando resultados en la parte superior y una vez se obtengan se mostrarán de manera automática para que el usuario pueda seguir desplazándose hacia abajo simulando un efecto de que la lista de resultados es infinita.
En el caso específico de las miniaturas de los productos, se mostrará una imagen fija que se sustituirá por la imagen real una vez esté disponible.
Seleccionar uno de los resultados hará que se muestren sus detalles.

El componente de búsqueda en la parte superior permite realizar búsqueda por texto, se despliega haciendo "tap" sobre el ícono de búsqueda y una vez desplegado presionando el ícono de cerrar vuelve a su estado inicial. En su estado desplegado permitirá al usuario realizar las búsquedas por texto por voz si presiona el ícono del micrófono.
En ambos casos una vez se introduzca el criterio de búsqueda, la aplicación guardará dicho criterio en las búsquedas recientes y realizará la búsqueda. Adicionalmente se muestra un ícono para permitir al usuario eliminar las búsquedas recientes o mostrando una confirmación antes de hacerlo según las buenas prácticas sugeridas por Google.
Adicionalmente teniendo en cuenta la cantidad de resultados que se pueden mostrar, cuando el usuario arrastre hacia arriba se ocultará 
automáticamente la barra superior, la cual volverá a mostrarse cuando el usuario arrastre la lista de resultados hacia abajo.

Si el criterio de búsqueda no produjo resultados se mostrará un mensaje fijo indicando al usuario que no hay resultados, de manera similar si se produjo un error al realizar la búsqueda, se mostrará el mismo mensaje ya que en efecto no hay resultados para mostrar y adicionalmente se mostrará una alerta informando del error, la cual podrá ser descartada por el usuario.
En ambos casos el usuario siempre podrá realizar una nueva búsqueda.

### Pantalla de visualización de detalles

Esta pantalla se muestra una vez el usuario haya seleccionado un producto. Para mejorar la fluidez, inicialmente se muestran los datos del producto que ya están en memoria pues forman parte de los resultados de la búsqueda realizada en la pantalla anterior.
Luego se cargan las imágenes del producto y la descripción, proceso durante el cual se mostrará al usuario un indicador para que entienda que se están descargando dichos datos, en el caso específico de las imágenes, se mostrará una imagen fija que se sustituirá por la imagen real una vez esté disponible.

Las imágenes del producto se muestran en la sección superior" donde el usuario podrá desplazarse por ellas arrastrando de forma horizontal, de esta forma las imágenes entrarán y saldrán de la parte visible con un efecto "zoom out".
Adicionalmente se muestra un indicador en la parte inferior de las imágenes, el cual mostrará el total de elementos y el que se está mostrando en cada momento, indicando al usuario si puede o no navegar hacia la derecha o izquierda en dependencia de la cantidad de imágenes con que cuente el producto.
Teniendo en cuenta que las descripciones de los prouctos son significativamente grandes, la pantalla de detalles se desplazará hacia arriba si el usuario arrastra en esa dirección, permitiendo mostrar la totalidad de la descripción y ocultando la barra superior para disponer de más espacio. Si el usuario arrastra hacia abajo los detalles volverán a su estado inicial.

La pantalla de detalles permitirá al usuario navegar hacia la búsqueda de resultados a través del botón "atrás" del dispositivo o de una opción similar en la barra superior de la aplicación.

En caso de producirse un error al intentar cargar los detalles del producto, se informará al usuario a través de una alerta que podrá descartar, en ese caso, para mejorar la experiencia, se mantendrán en pantalla los datos previamente cargados y se mostrará la miniatura del producto que también había sido previamente cargada.

## ARQUITECTURA

### Visión general

La arquitectura se basa en la actualizada [guía de arquitectura de aplicaciones](https://developer.android.com/jetpack/guide#connect-viewmodel-repository) de Google, teniendo como pilares principales el patrón arquitectónico MVVM (Model-View-ViewModel), el concepto de Clean Architecture de Robert C. Martin y los principios SOLID.
Dentro de los componentes de la arquitectura que promueve Google utilizados en el presente desarrollo se encuentran entre otros:
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Databinding](https://developer.android.com/topic/libraries/data-binding)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

### Clean Architecture

Hay 5 paquetes principales para ayudar a separar el código. Ellos son "di", "domain", "framework", "interactors" y "presentation".

- DI: Contiene la inyección de dependencias de forma transversal para toda la aplicación, al ser una aplicación pequeña, por motivos de simplicidad y tiempo no se separaron los distintos alcances (scopes) y se realizó la organización directamente por módulos.
- DOMAIN: Es el centro de la arquitectura y contiene las entidades de negocio, no depende de ningún otro paquete o componente.
- FRAMEWORK: Contiene las fuentes de datos que dependen directamente de la plataforma, por ejemplo Retrofit y SearchRecentSuggestionsProvider, en los sub paquetes "networking" y "android" respectivamente.
- INTERACTORS: Contiene los casos de uso o "interactors" que se comunican directamente con la capa "framework" para gestionar os datos.
- PRESENTATION: Contiene los componentes de la capa de presentación.


### Lenguaje 

Se utiliza Kotlin como lenguaje de desarrollo. 


### Dependencias & Bibliotecas

Se trabaja bajo el principio de utilizar la menor cantidad de bibliotecas adicionales posibles para mantener la aplicación con un peso mínimo, de igual manera se intenta evitar la utilización de archivos "raw" y los iconos se manejan en formato "*.svg".


#### Dagger 2
> Se utiliza para implementar la inyección de dependencias, contribuyendo a una arquitectura significativamente más desacoplada y fácil de probar.

#### Rx Java / Android 
> Se utiliza para llevar a cabo las operaciones asíncronas. 

#### Retrofit 
> Se utiliza para manejar la comunicación con la API de Mercado Libre.

#### GSON 
> Se usará para convertir los elementos retornados por el servidor (json) a "data classes" de Kotlin

#### Pruebas unitarias

Para incrementar los niveles de calidad se le realizaron pruebas unitarias a los viewmodels e interactors, con un total de 57 casos de prueba donde se tuvieron en cuenta la gran mayoría de los escenarios bordes y se realizaron pruebas de mutación.
