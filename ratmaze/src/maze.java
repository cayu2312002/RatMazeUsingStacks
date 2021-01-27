import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
class rataenlaberinto {
    // Vector para guardar todas las posibles soluciones
    static Vector<String> caminosPosibles = new Vector<>();
    static String camino = "";
    static final int MAX = 20;
    static Stack<Integer> stackFilas = new Stack<>();
    static Stack<Integer> stackColumnas = new Stack<>();
    static Stack<String> stackCaminos = new Stack<>();
    static Stack<Integer> stackSalida = new Stack<>();
    // Funcion que retorna verdadero si el movimiento es seguro o de lo contrario retorna falso.
    static boolean esSeguro(int fila, int col, int[][] m, int n, boolean[][] visitados) {
        return fila != -1 && fila != n && col != -1 && col != n && !visitados[fila][col] && m[fila][col] != 0;
    }
    // Función que analiza cuantas posiciones entre arriba, abajo, derecha e izquierda son seguros.
    static int numeroSalidasPosibles(int fila, int col, int[][] m, int n, boolean[][] visitados) {
        int salidas = 0;
        if (esSeguro(fila + 1, col, m, n, visitados)) {
            salidas++;
        }
        if (esSeguro(fila - 1, col, m, n, visitados)) {
            salidas++;
        }
        if (esSeguro(fila, col + 1, m, n, visitados)) {
            salidas++;
        }
        if (esSeguro(fila, col - 1, m, n, visitados)) {
            salidas++;
        }
        return salidas;
    }
    // Funcion para encontrar y almacenar todos los caminos posibles.
    static void imprimirCaminoUtil(int fila, int col, int[][] m, int n, int filafinal, int columnafinal, boolean[][] visitados) {
        //se verifica que la primera casilla sea válida y se marca como visitada
        if (fila == -1 || fila == n || col == -1 || col == n || visitados[fila][col] || m[fila][col] == 0) {
            return;
        }
        visitados[fila][col] = true;
        //NO BORRAR
        stackSalida.push(-1); //controla la salida del do while
        //un ciclo que se repite hasta tener todos los caminos
        do {
            boolean caminoEncontrado = false;        //variable que controla cuando se ha encontrado un camino
            if (fila == filafinal - 1 && col == columnafinal - 1)   //Posicion final
            {
                caminosPosibles.add(camino);             //se guarda el camino y se actualiza la variable
                caminoEncontrado = true;                 //para regresar al lugar correcto
            }
            //si hay sólo una salida
            if (numeroSalidasPosibles(fila, col, m, n, visitados) == 1 && !caminoEncontrado) {
                //si la salida es por abajo
                if (esSeguro(fila + 1, col, m, n, visitados)) {
                    camino += '↓';                   //se agrega la direccion a la salida
                    fila += 1;                       //se mueve la posicion que vamos a revisar
                    visitados[fila][col] = true;     //se marca la casilla como visitada
                    //se hace igual con cada dirección del movimiento
                }
                //si la salida es por la izquierda
                else if (esSeguro(fila, col - 1, m, n, visitados)) {
                    camino += '←';
                    col -= 1;
                    visitados[fila][col] = true;
                }
                //si la salida es por la derecha
                else if (esSeguro(fila, col + 1, m, n, visitados)) {
                    camino += '→';
                    col += 1;
                    visitados[fila][col] = true;
                }
                //si la salida es por arriba
                else if (esSeguro(fila - 1, col, m, n, visitados)) {
                    camino += '↑';
                    fila -= 1;
                    visitados[fila][col] = true;
                }
            }
            //si se encontró un camino
            else if (caminoEncontrado) {
                //se realiza una copia del camino
                // para volver al lugar adecuado
                int filaAux = fila;   //se guardan variables auxiliares antes de desapilar para volver al lugar adecuado
                int colAux = col;
                int salidaActual;
                //se des apilan los ultimos valores guardados para realizar el siguiente movimiento después de
                //volver al lugar adecuado continuando con el laberinto
                try {
                    fila = stackFilas.pop();
                    col = stackColumnas.pop();
                    camino = stackCaminos.pop();
                    salidaActual = stackSalida.pop();
                } catch (EmptyStackException e) {
                    return;
                }
                //se llena la matriz de visitados en "false" para evitar problemas al recorrer nuevamente el laberinto
                for (int i = 0; i < visitados.length; i++) {
                    for (int j = 0; j < visitados.length; j++) {
                        visitados[i][j] = false;
                    }
                }
                //se controla un caso especial en el que la casilla final debe ser false y puede estar marcada true
                if (filaAux != filafinal - 1 || colAux != columnafinal - 1) {
                    visitados[filafinal - 1][columnafinal - 1] = false;
                }
                //se controla un caso especial en el que la casilla final debe ser true
                if (filaAux == filafinal - 1 && (colAux - 1 == columnafinal - 1 || colAux + 1 == columnafinal - 1)) {
                    visitados[filafinal - 1][columnafinal - 1] = true;
                }
                filaAux = fila;                      //se guardan nuevamente las variables auxiliares en la ultima casilla
                colAux = col;                        //del camino al que hemos regresado
                visitados[filaAux][colAux] = true;   //se marca dicha casila como visitada
                //siguiendo paso a paso el camino hacia atrás tendremos marcadas sin errores las casillas visitadas
                //necesarias al momento de regresar
                for (int i = 0; i < camino.length(); i++) {
                    if (camino.charAt(camino.length() - i - 1) == '↓') {
                        visitados[filaAux - 1][colAux] = true;
                        filaAux -= 1;
                    } else if (camino.charAt(camino.length() - (i + 1)) == '←') {
                        visitados[filaAux][colAux + 1] = true;
                        colAux += 1;
                    } else if (camino.charAt(camino.length() - (i + 1)) == '→') {
                        visitados[filaAux][colAux - 1] = true;
                        colAux -= 1;
                    } else if (camino.charAt(camino.length() - (i + 1)) == '↑') {
                        visitados[filaAux + 1][colAux] = true;
                        filaAux += 1;
                    }
                }
                //se realiza el siguiente movimiento
                boolean sw = false;  //una variable switch para determinar cuando se realiza un movimiento
                while (!sw && !stackSalida.empty()) {
                    if (esSeguro(fila + 1, col, m, n, visitados) && salidaActual == 0) {
                        camino += '↓';
                        fila += 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por la izquierda
                    else if (esSeguro(fila, col - 1, m, n, visitados) && salidaActual == 1) {
                        camino += '←';
                        col -= 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por la derecha
                    else if (esSeguro(fila, col + 1, m, n, visitados) && salidaActual == 2) {
                        camino += '→';
                        col += 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por arriba
                    else if (esSeguro(fila - 1, col, m, n, visitados) && salidaActual == 3) {
                        camino += '↑';
                        fila -= 1;
                        visitados[fila][col] = true;
                        sw = true;
                    } else {
                        fila = stackFilas.pop();
                        col = stackColumnas.pop();
                        camino = stackCaminos.pop();
                        stackSalida.pop();
                    }
                }
            }
            //si hay más de una salida
            else if (numeroSalidasPosibles(fila, col, m, n, visitados) > 1) {
                visitados[fila][col] = true; //se marca la casilla actual como visitada
                //se apilan los movimientos posibles desde esa casilla verificando si se puede mover hacia cada direccion
                //y con el elemento de stackSalida que determina el lado al que se va a mover al momento de desapilar
                if (esSeguro(fila + 1, col, m, n, visitados)) { //si se puede salir hacia abajo
                    stackCaminos.push(camino);
                    stackFilas.push(fila);
                    stackColumnas.push(col);
                    stackSalida.push(0);  //el cero corresponde a una salida hacia abajo
                }
                if (esSeguro(fila, col - 1, m, n, visitados)) { //si se puede salir hacia la izquierda
                    stackCaminos.push(camino);
                    stackFilas.push(fila);
                    stackColumnas.push(col);
                    stackSalida.push(1); // el 1 corresponde a una salida a la izquierda
                }
                if (esSeguro(fila, col + 1, m, n, visitados)) {
                    stackCaminos.push(camino);
                    stackFilas.push(fila);
                    stackColumnas.push(col);
                    stackSalida.push(2); //el 2 correpsonde a una salida a la derecha
                }
                if (esSeguro(fila - 1, col, m, n, visitados)) {
                    stackCaminos.push(camino);
                    stackFilas.push(fila);
                    stackColumnas.push(col);
                    stackSalida.push(3); //el 3 corresponde a una salida hacia arriba
                }
                //se des apilan los ultimos elementos para continuar por dicho camino
                fila = stackFilas.pop();
                col = stackColumnas.pop();
                camino = stackCaminos.pop();
                int salidaActual = stackSalida.pop();

                //se realiza el movimiento comprobando con el valor de "salidaActual" la direccion a la que debe mover
                boolean sw = false; //un switch controla cuando ya se realizó un movimiento
                while (!sw) {
                    if (esSeguro(fila + 1, col, m, n, visitados) && salidaActual == 0) {
                        camino += '↓';
                        fila += 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por la izquierda
                    else if (esSeguro(fila, col - 1, m, n, visitados) && salidaActual == 1) {
                        camino += '←';
                        col -= 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por la derecha
                    else if (esSeguro(fila, col + 1, m, n, visitados) && salidaActual == 2) {
                        camino += '→';
                        col += 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por arriba
                    else if (esSeguro(fila - 1, col, m, n, visitados) && salidaActual == 3) {
                        camino += '↑';
                        fila -= 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                }
            }
            //si no hay una salida
            else if (numeroSalidasPosibles(fila, col, m, n, visitados) == 0) {
                //se realiza una copia exacta del camino
                int salidaActual; //variable usada para verificar hacia qué dirección será el proximo movimiento

                //se controla un caso especial en el que la casila final debe ser false y estaba marcada como true
                if (fila != filafinal - 1 || col != columnafinal - 1) {
                    visitados[filafinal - 1][columnafinal - 1] = false;
                }
                //se desapilan los valores necesarios para vover al lugar correcto
                try {
                    fila = stackFilas.pop();
                    col = stackColumnas.pop();
                    camino = stackCaminos.pop();
                    salidaActual = stackSalida.pop();
                } catch (EmptyStackException e) {
                    return;
                }
                int filaAux = fila; //se definen valores auxiliares para actualizar la matriz de casillas visitadas
                int colAux = col;   //deacuerdo al nuevo camino tomado
                //se ponen todas las casillas de "visitados" en false
                for (int i = 0; i < visitados.length; i++) {
                    for (int j = 0; j < visitados.length; j++) {
                        visitados[i][j] = false;
                    }
                }
                visitados[filaAux][colAux] = true; //se actualiza la ultima casilla del nuevo camino como visitada
                //se verifica el camino tomado hacia atrás para evitar errores en la matriz de casillas visitadas
                for (int i = 0; i < camino.length(); i++) {
                    if (camino.charAt(camino.length() - i - 1) == '↓') {
                        visitados[filaAux - 1][colAux] = true;
                        filaAux -= 1;
                    } else if (camino.charAt(camino.length() - (i + 1)) == '←') {
                        visitados[filaAux][colAux + 1] = true;
                        colAux += 1;
                    } else if (camino.charAt(camino.length() - (i + 1)) == '→') {
                        visitados[filaAux][colAux - 1] = true;
                        colAux -= 1;
                    } else if (camino.charAt(camino.length() - (i + 1)) == '↑') {
                        visitados[filaAux + 1][colAux] = true;
                        filaAux += 1;
                    }
                }
                //se realiza el siguiente movimiento
                boolean sw = false; //una variable switch determina cuando ya se realizó un movimiento
                while (!sw) {
                    if (esSeguro(fila + 1, col, m, n, visitados) && salidaActual == 0) {
                        camino += '↓';
                        fila += 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por la izquierda
                    else if (esSeguro(fila, col - 1, m, n, visitados) && salidaActual == 1) {
                        camino += '←';
                        col -= 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por la derecha
                    else if (esSeguro(fila, col + 1, m, n, visitados) && salidaActual == 2) {
                        camino += '→';
                        col += 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                    //si la salida es por arriba
                    else if (esSeguro(fila - 1, col, m, n, visitados) && salidaActual == 3) {
                        camino += '↑';
                        fila -= 1;
                        visitados[fila][col] = true;
                        sw = true;
                    }
                }
            }
        } while (caminosPosibles.isEmpty() || !stackSalida.empty());
    }
    // Función que imprime todos los caminos válidos, los clasifica en el ranking, le pregunta al usuario cual desea ver e imprime los deseados por el usuario.
    static void imprimirCamino(int[][] m, int n, int filainicial, int columnainicial, int filafinal,
                               int columnafinal) {
        boolean[][] visitados = new boolean[n][MAX];
        // Llame a la función de utilidad para encontrar los caminos válidos
        imprimirCaminoUtil(filainicial - 1, columnainicial - 1, m, n, filafinal, columnafinal, visitados); //En fila y col ponemos la posicion inicial de la rata
        int numerodecaminos = caminosPosibles.size();
        System.out.println("El numero de caminos posibles para ir desde: ("+ filainicial+ ","+columnainicial+") hasta ("+filafinal+","+columnafinal+") son: " + numerodecaminos);
        //For para organizar los caminos en orden de menos pasos a mas pasos
        for (int i = 0; i < numerodecaminos - 1; i++) {
            for (int j = 0; j < numerodecaminos - 1 - i; j++) {
                if ((caminosPosibles.elementAt(j).length()) > (caminosPosibles.elementAt(j + 1).length())) {
                    caminosPosibles.add(j, caminosPosibles.elementAt(j + 1));
                    caminosPosibles.removeElementAt(j + 2);
                }
            }
        }
        // Imprime todos los caminos posibles
        System.out.println("A continuación le mostramos todos los caminos posibles, separados entre sí por un espacio: ");
        for (String caminosPosible : caminosPosibles) System.out.print(caminosPosible + " ");
        Vector<Integer> clasi = new Vector<>();
        for (String caminosposible : caminosPosibles) {
            if (!(clasi.contains(caminosposible.length()))) {
                clasi.add(caminosposible.length());
            }
        }
        if (clasi.size() > 0) {
            System.out.println();
            System.out.println("Hay: " + caminosPosibles.size() + " camino(s) posible(s), con " + clasi.size() + " distancias diferentes, ingrese cual/cuales quiere ver, considerando el ranking, siendo 1 el/los mejor(es), y " + clasi.size() + " el/los mas largos.");
            Scanner reader = new Scanner(System.in);
            int deseado = 0;
            boolean bien = false;
            while (!bien) {
                deseado = reader.nextInt();
                if (deseado <= clasi.size() && deseado > 0) {
                    bien = true;
                } else {
                    System.out.println("Su elección no es valida, digitela de nuevo: ");
                }
            }
            System.out.println("La(s) solucion(es) que ocupan la posicion " + deseado + " en el ranking tienen una longitud de "+clasi.elementAt(deseado-1)+ " pasos, y esa(s) es/son: ");
            for (String caminoposible : caminosPosibles) {
                if (caminoposible.length() == clasi.elementAt(deseado - 1)) {
                    System.out.print(caminoposible + " ");
                }
            }
        } else {
            System.out.println("No hay caminos disponibles.");
        }
    }
    //Metodo principal que busca el solver "matriz.txt", y en caso de encontrarlo, lee los datos, imprime la matriz leida e invoca el metodo imprimirCamino; en caso de no encontrarlo controla las excepciones.
    public static void main(String[] args) {
        int[][] matriz;
        try {
            BufferedReader br = new BufferedReader(new FileReader("matriz.txt"));
            //Primera linea nos dice longitud de la matriz
            String linea = br.readLine();
            int longitud = Integer.parseInt(linea);
            matriz = new int[longitud][longitud];
            //Segunda linea nos dice la fila incial en la que se encuentra el raton.
            linea = br.readLine();
            int filainicial = Integer.parseInt(linea);
            //Tercera linea nos dice la columna incial en la que se encuentra el raton.
            linea = br.readLine();
            int columnainicial = Integer.parseInt(linea);
            //Cuarta linea nos dice la fila a donde debe llegar el raton.
            linea = br.readLine();
            int filafinal = Integer.parseInt(linea);
            //Quinta linea nos dice la columna a donde debe llegar el raton.
            linea = br.readLine();
            int columnafinal = Integer.parseInt(linea);
            //Las siguientes lineas son filas de la matriz
            linea = br.readLine();
            int fila = 0; //Para recorrer las filas de la matriz
            while (linea != null) {
                /*
                 * Tenemos todos los enteros JUNTOS en el String linea.
                 * Con split() los SEPARAMOS en un array donde cada entero
                 * es un String individual. Con un bucle, los parseamos a Integer
                 * para guardarlos en la matriz
                 */
                String[] enteros = linea.split(" ");
                for (int i = 0; i < enteros.length; i++)
                    matriz[fila][i] = Integer.parseInt(enteros[i]);
                fila++; //Incrementamos fila para la próxima línea de enteros
                linea = br.readLine(); //Leemos siguiente línea
            }
            br.close(); //Cerramos el lector de ficheros
            //Mostramos la matriz leída
            System.out.println("La matriz ingresada es:");
            for (int i = 0; i < longitud; i++) {
                for (int j = 0; j < longitud; j++)
                    System.out.print(matriz[i][j] + " ");
                System.out.println();
            }
            int n = matriz.length;
            imprimirCamino(matriz, n, filainicial, columnainicial, filafinal, columnafinal);
        } catch (FileNotFoundException e) {
            System.out.println("No se encuentra archivo");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("No se pudo convertir a entero");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error accediendo al archivo.");
            e.printStackTrace();
        }
    }
}