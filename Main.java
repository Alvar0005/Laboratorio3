import java.util.*;
import java.io.*;
public class Main{
    public static class Game{
        private String name;
        private String category;
        private int price;
        private int quality;

        public Game(String name, String category, int price, int quality){
            this.name=name;
            this.category=category;
            this.price=price;
            this.quality=quality;
        }
        public String getName(){return name;}
        public String getCategory(){return category;}
        public int getPrice(){return price;}
        public int getQuality(){return quality;}
        public String toString(){
            return name + " | " + category + " | $" + price + " | Calidad: " + quality;
        }
    }
    public static class Dataset{
        ArrayList<Game> data;
        String sortedByAttribute;

        public Dataset(ArrayList<Game> data){
            this.data=data;
            this.sortedByAttribute="";
        }
        public ArrayList<Game> getGamesByPrice(int precio){
            ArrayList<Game> res = new ArrayList<>();
            if(sortedByAttribute.equals("precio")){
                int izq=0, der=data.size()-1, mid;
                while(izq<=der){
                    mid=(izq+der)/2;
                    if(data.get(mid).getPrice()==precio){
                        int i=mid;
                        while(i>=0 && data.get(i).getPrice()==precio){
                            res.add(0, data.get(i));
                            i--;
                        }
                        i=mid+1;
                        while(i<data.size() && data.get(i).getPrice()==precio){
                            res.add(data.get(i));
                            i++;
                        }
                        break;
                    }else if(data.get(mid).getPrice()<precio){
                        izq=mid+1;
                    }else{
                        der=mid-1;
                    }
                }
            }else{
                for(Game juego:data){
                    if(juego.getPrice()==precio){
                        res.add(juego);
                    }
                }
            }
            return res;
        }
        public ArrayList<Game> getGamesByPriceRange(int pMin, int pMax){
            ArrayList<Game> res = new ArrayList<>();
            for(Game juego:data){
                int p=juego.getPrice();
                if(p>=pMin && p<=pMax){
                    res.add(juego);
                }
            }
            return res;
        }
        public ArrayList<Game> getGamesByCategory(String cat){
            ArrayList<Game> res = new ArrayList<>();
            for(Game juego : data){
                if(juego.getCategory().equals(cat)){
                    res.add(juego);
                }
            }
            return res;
        }
        public ArrayList<Game> getGamesByQuality(int cal){
            ArrayList<Game> res = new ArrayList<>();
            for(Game juego : data){
                if(juego.getQuality()==cal){
                    res.add(juego);
                }
            }
            return res;
        }
        public double sortByAlgorithm(String algoritmo, String atributo) {
            Comparator<Game> comp;
            switch (atributo) {
                case "categoría":
                    comp = Comparator.comparing(Game::getCategory);
                    break;
                case "quality":
                    comp = Comparator.comparingInt(Game::getQuality);
                    break;
                case "precio":
                    comp = Comparator.comparingInt(Game::getPrice);
                    break;
                case "nombre":
                    comp = Comparator.comparing(Game::getName);
                    break;
                default:
                    comp = Comparator.comparing(Game::getName);
                    atributo = "nombre";
            }
            long inicio = System.nanoTime();
            switch (algoritmo) {
                case "bubbleSort":
                    bubbleSort(data, comp);
                    break;
                case "insertionSort":
                    insertionSort(data, comp);
                    break;
                case "selectionSort":
                    selectionSort(data, comp);
                    break;
                case "mergeSort":
                    data = new ArrayList<>(mergeSort(data, comp));
                    break;
                case "quickSort":
                    quickSort(data, 0, data.size() - 1, comp);
                    break;
                case "countingSort":
                    if (atributo.equals("quality")) {
                        countingSort();
                        // Modifica countingSort para no imprimir tiempo internamente
                        break;
                    } else {
                        collectionSort(comp);
                    }
                    break;
                default:
                    collectionSort(comp);
                    break;
            }
            long fin = System.nanoTime();
            double tiempoSegundos = (fin - inicio) / 1e9;
            sortedByAttribute = atributo;
            imprimir();
            System.out.printf("Tiempo %s: %.3f segundos\n", algoritmo, tiempoSegundos);
            System.out.println("Datos ordenados por " + atributo + " usando " + algoritmo);
            return tiempoSegundos;
        }
        private void bubbleSort(List<Game> lista, Comparator<Game> comp){
            long inicio=System.nanoTime();
            int tam=lista.size();
            for(int i=0; i<tam-1; i++){
                for(int j=0; j<tam-i-1; j++){
                    if(comp.compare(lista.get(j), lista.get(j + 1))>0){
                        Collections.swap(lista, j, j + 1);
                    }
                }
            }
            long fin=System.nanoTime();
            System.out.printf("Tiempo bubbleSort: %.3f segundos\n", (fin-inicio)/1e9);
        }
        private void insertionSort(List<Game> lista, Comparator<Game> comp){
            long inicio=System.nanoTime();
            int tam=lista.size();
            for(int i=1; i<tam; i++){
                Game act=lista.get(i);
                int j=i-1;
                while(j>=0 && comp.compare(lista.get(j), act)>0){
                    lista.set(j+1, lista.get(j));
                    j--;
                }
                lista.set(j + 1, act);
            }
            long fin=System.nanoTime();
            System.out.printf("Tiempo insertionSort: %.3f segundos\n", (fin-inicio)/1e9);
        }
        private void selectionSort(List<Game> lista, Comparator<Game> comp){
            long inicio=System.nanoTime();
            int tam=lista.size();
            for(int i=0; i<tam-1; i++){
                int indMin=i;
                for(int j=i+1; j<tam; j++){
                    if(comp.compare(lista.get(j), lista.get(indMin))<0){
                        indMin=j;
                    }
                }
                Collections.swap(lista, i, indMin);
            }
            long fin=System.nanoTime();
            System.out.printf("Tiempo selectionSort: %.3f segundos\n", (fin-inicio)/1e9);
        }
        private void quickSort(List<Game> lista, int ini, int fin, Comparator<Game> comp){
            long inicio=System.nanoTime();
            quickSortRec(lista, ini, fin, comp);
            long end=System.nanoTime();
            System.out.printf("Tiempo quickSort: %.3f segundos\n", (end-inicio)/1e9);
        }
        private void quickSortRec(List<Game> lista, int ini, int fin, Comparator<Game> comp){
            if(ini<fin){
                int piv=particionar(lista, ini, fin, comp);
                quickSortRec(lista, ini, piv-1, comp);
                quickSortRec(lista, piv+1, fin, comp);
            }
        }
        private int particionar(List<Game> lista, int ini, int fin, Comparator<Game> comp){
            Game pivote=lista.get(fin);
            int i=ini-1;
            for(int j=ini; j<fin; j++){
                if(comp.compare(lista.get(j), pivote)<=0){
                    i++;
                    Collections.swap(lista, i, j);
                }
            }
            Collections.swap(lista, i+1, fin);
            return i + 1;
        }
        private List<Game> mergeSort(List<Game> lista, Comparator<Game> comp){
            long inicio=System.nanoTime();
            List<Game> resultado = mergeSortRec(lista, comp);
            long fin=System.nanoTime();
            System.out.printf("Tiempo mergeSort: %.3f segundos\n", (fin-inicio)/1e9);
            return resultado;
        }
        private List<Game> mergeSortRec(List<Game> lista, Comparator<Game> comp){
            if(lista.size()<=1){
                return lista;
            }
            int mitad=lista.size()/2;
            List<Game> izq=mergeSortRec(lista.subList(0, mitad), comp);
            List<Game> der=mergeSortRec(lista.subList(mitad, lista.size()), comp);
            return mergear(izq, der, comp);
        }
        private List<Game> mergear(List<Game> izq, List<Game> der, Comparator<Game> comp){
            List<Game> resultado = new ArrayList<>();
            int i=0, j=0;
            while(i<izq.size() && j<der.size()){
                if(comp.compare(izq.get(i), der.get(j))<=0){
                    resultado.add(izq.get(i));
                    i++;
                }
                else{
                    resultado.add(der.get(j));
                    j++;
                }
            }
            while(i<izq.size()){
                resultado.add(izq.get(i++));
            }
            while(j<der.size()){
                resultado.add(der.get(j++));
            }
            return resultado;
        }
        public void countingSort(){
            long inicio = System.nanoTime();

            int maxCalidad = 100;
            int[] conteo = new int[maxCalidad + 1];
            for(Game juego : data){
                conteo[juego.getQuality()]++;
            }
            for(int i = 1; i <= maxCalidad; i++){
                conteo[i] += conteo[i - 1];
            }
            Game[] ordenado = new Game[data.size()];
            for(int i = data.size() - 1; i >= 0; i--){
                Game juego = data.get(i);
                int pos = conteo[juego.getQuality()] - 1;
                ordenado[pos] = juego;
                conteo[juego.getQuality()]--;
            }
            data.clear();
            Collections.addAll(data, ordenado);

            this.sortedByAttribute = "quality";

            long fin = System.nanoTime();
            System.out.printf("Tiempo countingSort: %.3f segundos\n", (fin - inicio) / 1e9);
        }
        private void collectionSort(Comparator<Game> comp){
            long inicio=System.nanoTime();
            Collections.sort(data, comp);
            long fin=System.nanoTime();
            System.out.printf("Tiempo collectionSort: %.3f segundos\n", (fin - inicio) / 1e9);
        }
        public void imprimir(){
            for(Game j : data){
                System.out.println(j);
            }
        }
    }
    public static class GenerateData{
        private static final String[] iniciales={"Dragon", "Empire", "Quest", "Galaxy", "Legends", "Warrior", "War", "Summoners", "Pïxel", "My", "Assasins", "Gun Game", "Silent", "League Of", "Age Of", "Devil", "The Binding Of:", "God Of"};
        private static final String[] finales={"Dragon", "Empire", "Quest", "Galaxy", "Legends", "Warrior", "War", "Sky", "Village", "World", "Emperor", "Online", "Inmolation", "Hill", "Games", "Terrain", "Alpha", "Zaiross"};
        private static final String[] cats={"Acción", "Aventura", "Estrategia", "RPG", "Deportes", "Simulación", "Terror", "SandBox", "Cooperativo", "Puzle"};
        private static final String[] sufijos={"Raknarok", "Rebird", "Deluxe Edition", "Collection Edition", "Ultimate Edition", "Boosted", "Origins", "Dead Trigger", "Unchained Death", ": SKL", ": PT"};
        private static final String[] romanos={"II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV"};

        public static ArrayList<Game> generateGames(int n){
            ArrayList<Game> juegos = new ArrayList<>();
            Set<String> usados = new HashSet<>();
            Random r = new Random();
            for(int i=0; i<n; i++){
                String palabra1=iniciales[r.nextInt(iniciales.length)];
                String palabra2;
                do{
                    palabra2=finales[r.nextInt(finales.length)];
                } while(palabra1.equals(palabra2));
                String base=palabra1 + " " + palabra2;
                String nombre=base;
                int intentos=0;
                while(usados.contains(nombre)){
                    int tipo=r.nextInt(3);
                    switch(tipo){
                        case 0:
                            nombre=base + " " + romanos[r.nextInt(romanos.length)];
                            break;
                        case 1:
                            nombre=base + ": " + sufijos[r.nextInt(sufijos.length)];
                            break;
                        case 2:
                            nombre=base + " " + (r.nextInt(3031 - 1940 + 1) + 1940);
                            break;
                        case 3:
                            nombre=base + " " + (r.nextInt(1000 - 2 + 1) + 2);
                            break;
                        case 4:
                            nombre=base + ": " + sufijos[r.nextInt(sufijos.length)] + " " + romanos[r.nextInt(romanos.length)];
                            break;
                        case 5:
                            nombre=base + ": " + sufijos[r.nextInt(sufijos.length)] + " " + romanos[r.nextInt(romanos.length)] + " (" + (r.nextInt(2025 - 1940 + 1) + 1940) + ")";
                            break;
                    }
                    if(++intentos>10){
                        break;
                    }
                }
                usados.add(nombre);
                String cat=cats[r.nextInt(cats.length)];
                int precio=r.nextInt(69001) + 1000;
                int cal=r.nextInt(101);
                juegos.add(new Game(nombre, cat, precio, cal));
            }
            return juegos;
        }
    }
    public static ArrayList<Game> leerJuegosDesdeArchivo(String rutaArchivo) {
        ArrayList<Game> juegos = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if(partes.length < 4) continue;
                String nombre = partes[0].trim();
                String categoria = partes[1].trim();
                String precioStr = partes[2].trim().replace("$", "");
                int precio = Integer.parseInt(precioStr);
                String calidadStr = partes[3].trim().replace("Calidad:", "").trim();
                int calidad = Integer.parseInt(calidadStr);

                juegos.add(new Game(nombre, categoria, precio, calidad));
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        return juegos;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Game> juegos = null;
        int opArchivo = -1;
        while (true) {
            System.out.println("Seleccione archivo de juegos a cargar:");
            System.out.println("1) Juegos 10^2");
            System.out.println("2) Juegos 10^4");
            System.out.println("3) Juegos 10^6");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            opArchivo = sc.nextInt();
            sc.nextLine();
            if(opArchivo==0){
                System.out.println("Saliendo...");
                sc.close();
                return;
            }
            String rutaArchivo = null;
            switch (opArchivo){
                case 1: rutaArchivo = "Juegos 10^2.txt"; break;
                case 2: rutaArchivo = "Juegos 10^4.txt"; break;
                case 3: rutaArchivo = "Juegos 10^6.txt"; break;
                default:
                    System.out.println("Opción inválida");
                    continue;
            }
            juegos = leerJuegosDesdeArchivo(rutaArchivo);
            if (juegos == null || juegos.isEmpty()) {
                System.out.println("No se pudieron cargar los juegos del archivo.");
                continue;
            }
            break;
        }
        Dataset ds = new Dataset(juegos);
        while (true) {
            System.out.println("\nSeleccione algoritmo para ordenar:");
            System.out.println("1) Bubble Sort");
            System.out.println("2) Insertion Sort");
            System.out.println("3) Selection Sort");
            System.out.println("4) Merge Sort");
            System.out.println("5) Quick Sort");
            System.out.println("6) Counting Sort (solo calidad)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            int opAlg = sc.nextInt();
            sc.nextLine();
            if (opAlg == 0) {
                System.out.println("Saliendo...");
                break;
            }
            String alg;
            switch (opAlg) {
                case 1: alg = "bubbleSort"; break;
                case 2: alg = "insertionSort"; break;
                case 3: alg = "selectionSort"; break;
                case 4: alg = "mergeSort"; break;
                case 5: alg = "quickSort"; break;
                case 6: alg = "countingSort"; break;
                default:
                    System.out.println("Opción inválida");
                    continue;
            }
            System.out.println("Seleccione atributo:");
            System.out.println("1) Precio");
            System.out.println("2) Categoría");
            System.out.println("3) Calidad");
            System.out.println("4) Nombre");
            System.out.print("Opción: ");
            int opAtributo = sc.nextInt();
            sc.nextLine();
            String atr;
            switch (opAtributo) {
                case 1: atr = "precio"; break;
                case 2: atr = "categoría"; break;
                case 3: atr = "quality"; break;
                case 4: atr = "nombre"; break;
                default:
                    System.out.println("Opción inválida");
                    continue;
            }
            double tiempoSegundos = ds.sortByAlgorithm(alg, atr);
            if (opArchivo != 3) {  // No imprimir si es archivo 3 (1 millón)
                ds.imprimir();
            }
            System.out.printf("Tiempo %s: %.3f segundos\n", alg, tiempoSegundos);
            System.out.println("Datos ordenados por " + atr + " usando " + alg);
        }
        sc.close();
    }
}