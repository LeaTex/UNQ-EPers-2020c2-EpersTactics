# Entrega extra - Blockchain

Luego de un tiempo de implementado el juego, la empresa consigue un patrocinador dispuesto a colaborar ofreciendo productos de merchandising (remeras, vasos, tazas, llaveros, stickers, etc.) a cambio de banners publicitarios que se incluirán en el front.

<p align="center">
  <img src="cointower.jpg" />
</p>

Estos productos estarán disponibles en una tienda virtual, y las parties podrán adquirirlos con créditos que obtendrán cada vez que ganen una pelea. A su vez las parties podrán comprarse y venderse los productos en forma privada.

A fin de dar transparencia al intercambio y distribución de créditos, y para permitir que los valores puedan ser auditados por el patrocinador, se decide llevar los registros de movimientos en una blockchain privada.

Cada integrante de la red podrá participar de la gestión de dicha blockchain ya sea como nodo minero o simplemente como nodo validador.
En una primera etapa, como prueba de concepto, y dado que las parties tienen poco poder de procesamiento, solo funcionarán como nodos simples, que validen bloques, siendo los servidores del front los encargados de guardar las transacciones pendientes, y minar los nuevos bloques.

Para esto, se deberán implementar los siguientes cambios:

### Dirección (billetera)

Cada Party tendrá una "billetera", representada como una dirección. Esto permitirá que las transacciones sean semi-anónimas ya que no habrá forma de asociar una dirección con su dueño.
La Party creará su billetera al momento de registrarse en el juego, y sólo la informará cuando se le solicite para una transacción.

En esta primera etapa la dirección será un hash que represente el nombre de la party (público) más algún valor que solo la party conozca.
En próximas entregas la billetera será en realidad un par de claves públicas y privadas, que permitirán firmar las transacciones y así validar su titularidad, dando mayor seguridad a las operaciones.

### PartyService

- `billeteraDeParty(id: Long): String`: devuelve la dirección de la billetera de la Party correspondiente.

### Transacción

Representa el envío de créditos desde el servidor a una party, o de una party a otra. Tiene los siguientes datos:

- `timestamp`: fecha y hora en que se ralizó la transacción.
- `origen`: dirección desde se envían los créditos.
- `destino`: dirección a la cual se envían los créditos.
- `cantidad`: cantidad de créditos enviados.

En esta primera etapa del desarrollo será responsabilidad del minero controlar los saldos disponibles en cada billetera y dar validez a la transacción en sí misma.

### Bloque

Representa un bloque de la blockchain, y contendrá un conjunto de transacciones. Un bloque tiene:

- `número`: identificador secuencial del bloque, indica su posición en la cadena.
- `timestamp`: fecha y hora en que se minó el bloque.
- `previousHash`: hash del bloque anterior.
- `transacciones`: conjunto de transacciones incluidas.
- `nonce`: valor que representa la "prueba de trabajo" y permite validar la integridad del bloque.

### Servicio de Blockchain

Se creará un `BlockchainService` para responder a las interacciones con el minero, guardando y validando los nuevos bloques. Al momento de crearse se establecerá la dificultad de minado a utilizar. Esto indica con cuántos caracteres `0` debe iniciar el hash obtenido según la prueba de trabajo. Por ejemplo una dificultad de 3 significa que el resultado debe ser algo como `'000axg56fghjdk93...'`.
Implementará los siguientes métodos:

- `agregarBloque(numero: Long, timestamp: Date, trxs: List<Transaccion>, nonce: Int): Long`: Agrega un nuevo bloque a la blockchain. El bloque solo será agregado si pasa la validación (chequeo de la prueba de trabajo), en caso contrario se retornará 0.
- `validarBloque(numero: Long, timestamp: Date, previousHash: String, trxs: List<Transaccion>, nonce: Int): Boolean`: realiza el cálculo del hash del bloque con los datos dados, y indica si el mismo cumple las condiciones de dificultad. El hash buscado se calcula como: `sha256(numero + timestamp + previousHash + nonce + sha256(trxs))` . Se entiende como `previousHash` al hash del bloque `numero - 1` (o sea el anterior).
- `obtenerBloque(numero: Long): Bloque`: dado un número de bloque, devuelve el mismo con todas sus transacciones.
- `saldoDe(billetera: String): Long`: dada la dirección de una billetera, devuelve el saldo de la misma, según todos los movimientos presentes en la blockchain.
- `pruebaDeTrabajo(trxs: List<Transaccion>): Int`: En esta etapa inicial de la implementación los nodos podrán calcular la prueba de trabajo como si se tratase del minado de un nuevo bloque, y devolverán el nonce obtenido. En versiones futuras cuando los nodos puedan ser mineros se deberá incluir lógica más compleja que incluya el pago de la recompensa por minar.
