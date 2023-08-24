# JuegoDeDados_PrimeraFase
ApiRest Crud, Juego de dados  persistiendo MysqlWorkbench como base de datos, @Maven @Workbench @Swuagger 

El juego de dados se juega con dos dados. En caso de que el resultado de la suma de ambos dados sea menor o igual a 7, la partida será ganada, si no es perdida. Un jugador/a puede ver un listado de todas las tiradas que ha realizado y el porcentaje de éxito.

Para poder jugar al juego y realizar una tirada, un usuario debe registrarse con un nombre no repetido. Al crearse, se le asigna un identificador numérico único y una fecha de registro. Si el usuario así lo desea, puedes no añadir ningún nombre y se llamará “ANÓNIMO”. Puede haber más de un jugador “ANÓNIMO”.
Cada jugador puede ver un listado de todas las tiradas que ha hecho, con el valor de cada dado y si se ha ganado o no la partida. Además, puede saber su porcentaje de éxito por todas las tiradas que ha realizado.

No se puede eliminar una partida en concreto, pero sí se puede eliminar todo el listado de tiradas por un jugador/a.

El software debe permitir listar a todos los jugadores/as que hay en el sistema, el porcentaje de éxito de cada jugador/a y el porcentaje de éxito medio de todos los jugadores/as en el sistema.

El software debe respetar los principales patrones de diseño.

NOTAS

Tienes que tener en cuenta los siguientes detalles de construcción:

URL's:
POST: /players: crea un jugador/a.

PUT /players: modifica el nombre del jugador/a.

POST /players/{id}/games/ : un jugador/a específico realiza un tirón de los dados.

DELETE /players/{id}/games: elimina las tiradas del jugador/a.

GET /players/: devuelve el listado de todos los jugadores/as del sistema con su porcentaje medio de éxitos.

GET /players/{id}/games: devuelve el listado de jugadas por un jugador/a.

GET /players/ranking: devuelve el ranking medio de todos los jugadores/as del sistema. Es decir, el porcentaje medio de logros.

GET /players/ranking/loser: devuelve al jugador/a con peor porcentaje de éxito.

GET /players/ranking/winner: devuelve al jugador con peor porcentaje de éxito.

- Fase 1
Persistencia: utiliza como base de datos MySQL.

-Interfaz del proyecto en swuagger
![Swagger](https://github.com/Luiso-o/JuegoDeDados_SegundaFase/assets/128043647/63f467fb-d9c2-4251-b287-8c229dcbc36a)

-MiDB_partidas
![Mysql_Partidas](https://github.com/Luiso-o/JuegoDeDados_SegundaFase/assets/128043647/59e22b31-f8c9-47b2-8d2e-ae48ec58ed29)

-MiDB_jugadores
![Mysql_Jugadores](https://github.com/Luiso-o/JuegoDeDados_SegundaFase/assets/128043647/c483bd3c-0960-4935-8222-e78591078ed4)

IMPLEMENTACION DE UNIT TEST Y TEST DE INTEGRACION 

-Services
![Captura1](https://github.com/Luiso-o/JuegoDeDados_PrimeraFase/assets/128043647/81f82703-7472-4c4c-878d-9b6f325fb39d)

-Repositories
![Captura2](https://github.com/Luiso-o/JuegoDeDados_PrimeraFase/assets/128043647/00fd8051-0158-4d22-8653-81f8decd3467)

-Entities
![Captura3](https://github.com/Luiso-o/JuegoDeDados_PrimeraFase/assets/128043647/0af8e3a6-1c54-42ea-ab89-473857021215)

-Departaments
![Captura4](https://github.com/Luiso-o/JuegoDeDados_PrimeraFase/assets/128043647/960796fe-9dc6-4ad4-af9e-b8ddc2c1d478)

-Controller
![Captura5](https://github.com/Luiso-o/JuegoDeDados_PrimeraFase/assets/128043647/b5588a04-b58e-43e1-93d2-acd6863932cd)

