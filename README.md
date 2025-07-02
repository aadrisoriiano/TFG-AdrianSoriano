# TFG-AdrianSoriano

## 📊 Análisis de Calidad con SonarCloud

## Reportes automáticos de calidad de código

- [Detekt Report](https://aadrisoriiano.github.io/TFG-AdrianSoriano/detekt.html)

## Estado y calidad del código con SonarCloud


[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=bugs&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=code_smells&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=duplicated_lines_density&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=ncloc&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=reliability_rating&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=security_rating&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=sqale_index&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=sqale_rating&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=aadrisoriiano_TFG-AdrianSoriano&metric=vulnerabilities&token=3aae8f8068e7404570c849352dbf5025ae08f91a)](https://sonarcloud.io/summary/new_code?id=aadrisoriiano_TFG-AdrianSoriano)


## Resumen 
En la actualidad, casi todas las personas disponen de un dispositivo móvil. Por otro lado,
su sonido en entornos donde debería primar el silencio puede llegar a ser molesto o incluso
considerarse falta de respeto. Esto ocurre, por ejemplo, cuando estos dispositivos suenan
en bibliotecas, salas de reuniones, cines o teatros. Pese a que los sistemas operativos
permiten a los usuarios silenciar el móvil de manera manual, muchas veces se les olvida
hacerlo. Esto resulta en situaciones no deseadas e incómodas. La falta de automatización
y la dependencia de la memoria del usuario para evitar estos acontecimientos, hace que
el problema sea más habitual de lo esperado.
Para intentar resolver el problema descrito, este proyecto presenta el desarrollo de una
aplicación móvil para Android. La app automatiza la gestión del sonido del dispositivo,
y con ello, del modo silencio. La aplicación ofrece dos funcionalidades principales: la
posibilidad de programar una cuenta atrás, y la posibilidad de establecer franjas de control
de modos del sonido durante la semana. Durante la cuenta atrás o el horario definido, el
dispositivo permanecerá en un modo de sonido elegido por el usuario, desactivándose al
terminar, y restableciéndose el modo previo al inicio de cualquiera de las funcionalidades.
Los resultados obtenidos demuestran que la aplicación cumple con el objetivo de au-
tomatizar el control del modo de sonido en dispositivos Android. Permite reducir las
interrupciones no deseadas en entornos donde se requiere silencio. Se han implementado
el uso de temporizadores y horarios para gestionar el sonido de forma simple. Además, la
interfaz ha mostrado ser intuitiva, facilitando la interacción de los usuarios. También se
ha logrado un buen nivel de accesibilidad en la aplicación.
El proyecto ha supuesto un esfuerzo considerable, tanto en la implementación técnica de
la aplicación como en la elaboración de la memoria. La app está programada en Kotlin,
un lenguaje en el que ha sido necesario profundizar y adquirir nuevos conocimientos desde
cero. El código fuente cuenta con 2.295 líneas de código estructuradas en 13 clases que
cubren aspectos como la interfaz de usuario, la lógica de negocio, la gestión de base
de datos y la programación de alarmas. En total, el proyecto ha sido desarrollado a lo
largo de aproximadamente dos meses y medio, destinando más de la mitad de ese tiempo
a la programación.

## Instrucciones de instalación

 En primer lugar, es necesario descargar el archivo APK desde el repositorio de GitHub. Una vez instalada, se debe otorgar permiso de ejecución en segundo plano, lo cual es esencial para que la aplicación pueda gestionar los cambios de modo de sonido automáticamente, incluso cuando no está en primer plano. Finalmente, el usuario puede acceder a la aplicación y comenzar a utilizarla. 
