import java.util.*;
import java.io.*;

class Game{
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
class Dataset{
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
        if(sortedByAttribute.equals("categoría")){
            for(Game juego:data){
                if(juego.getCategory().equals(cat)){
                    res.add(juego);
                }
            }
        }else{
            for(Game juego:data){
                if(juego.getCategory().equals(cat)){
                    res.add(juego);
                }
            }
        }
        return res;
    }
    public ArrayList<Game> getGamesByQuality(int cal){
        ArrayList<Game> res = new ArrayList<>();
        if(sortedByAttribute.equals("quality")){
            for(Game juego:data){
                if(juego.getQuality()==cal){
                    res.add(juego);
                }
            }
        }else{
            for(Game juego:data){
                if(juego.getQuality()==cal){
                    res.add(juego);
                }
            }
        }
        return res;
    }
    public void sortByAlgorithm(String algoritmo, String atributo){
        Comparator<Game> comp;
        switch(atributo){
            case "categoría":
                comp=Comparator.comparing(Game::getCategory);
                break;
            case "quality":
                comp=Comparator.comparingInt(Game::getQuality);
                break;
            case "precio":
            default:
                comp=Comparator.comparingInt(Game::getPrice);
                atributo="precio";
        }
        switch(algoritmo){
            case "bubbleSort": bubbleSort(data, comp); break;
            case "insertionSort": insertionSort(data, comp); break;
            case "selectionSort": selectionSort(data, comp); break;
            case "mergeSort": data = new ArrayList(mergeSort(data, comp)); break;
            case "quickSort": quickSort(data, 0, data.size()-1, comp); break;
            case "countingSort":
                if(atributo.equals("quality")){
                    countingSort();
                    return;
                }else{data.sort(comp);}
                break;
            default:data.sort(comp);
        }
        sortedByAttribute=atributo;
    }
    static private void bubbleSort(List<Game> lista, Comparator<Game> comp){
    int tam=lista.size();
    for(int i=0; i<tam-1; i++){
        for(int j=0; j<tam-i-1; j++){
            if(comp.compare(lista.get(j), lista.get(j + 1))>0){
                Collections.swap(lista, j, j + 1);
            }
        }
    }
    }
    private void insertionSort(List<Game> lista, Comparator<Game> comp){
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
    }
    private void selectionSort(List<Game> lista, Comparator<Game> comp){
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
    }
    private void quickSort(List<Game> lista, int ini, int fin, Comparator<Game> comp){
    if(ini<fin){
        int piv=particionar(lista, ini, fin, comp);
        quickSort(lista, ini, piv-1, comp);
        quickSort(lista, piv+1, fin, comp);
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
    if(lista.size()<=1){
        return lista;
    }
    int mitad=lista.size()/2;
    List<Game> izq=mergeSort(lista.subList(0, mitad), comp);
    List<Game> der=mergeSort(lista.subList(mitad, lista.size()), comp);
    
    return mergear(izq, der, comp);
    }
    private List<Game> mergear(List<Game> izq, List<Game> der, Comparator<Game> comp){
    List<Game> resultado = new ArrayList<>();
    int i=0, j=0;

    while(i<izq.size() && j<der.size()){
        if(comp.compare(izq.get(i), der.get(j))<=0){
            resultado.add(izq.get(i));
            i++;
        }else{
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
    int maxCalidad=100;
    int[] conteo = new int[maxCalidad+1];
    for(Game juego : data){
        conteo[juego.getQuality()]++;
    }
    for(int i=1; i<=maxCalidad; i++){
        conteo[i]+=conteo[i-1];
    }
    Game[] ordenado = new Game[data.size()];
    for(int i=data.size()-1; i>=0; i--){
        Game juego=data.get(i);
        int pos=conteo[juego.getQuality()]-1;
        ordenado[pos]=juego;
        conteo[juego.getQuality()]--;
    }
    data.clear();
    Collections.addAll(data, ordenado);

    this.sortedByAttribute="quality";
    }
    public void imprimir(){
        for(Game j:data){
            System.out.println(j);
        }
    }
}
class GenerateData{
    public static ArrayList<Game> generateGames(int n){
        ArrayList<Game> juegos = new ArrayList<>();
        String[] nombres={"Dragon", "Empire", "Quest", "Galaxy", "Legends", "Warrior"};
        String[] cats={"Acción", "Aventura", "Estrategia", "RPG", "Deportes", "Simulación"};
        Random r = new Random();
        for(int i=0; i<n; i++){
            String nom=nombres[r.nextInt(nombres.length)] + nombres[r.nextInt(nombres.length)];
            String cat=cats[r.nextInt(cats.length)];
            int precio=r.nextInt(69001)+1000;
            int cal=r.nextInt(101);
            juegos.add(new Game(nom, cat, precio, cal));
        }
        return juegos;
    }
}

public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Dataset ds = new Dataset(GenerateData.generateGames(100));
        while(true){
            System.out.println("\n1) Mostrar todos los juegos"+
                                "\n2) Ordenar\n3) Buscar por precio"+
                                "\n4) Buscar por rango de precio"+
                                "\n5) Buscar por categoría"+
                                "\n6) Buscar por calidad"+
                                "\n0) Salir");
            System.out.print("Opción: ");
            int op = sc.nextInt();sc.nextLine();
            if(op==0){break;}
            switch(op){
                case 1:
                    ds.imprimir();
                    break;
                case 2:
                    System.out.print("Algoritmo (bubbleSort/insertionSort/selectionSort/mergeSort/quickSort/countingSort): ");
                    String alg = sc.nextLine();
                    System.out.print("Atributo (precio/categoría/quality): ");
                    String atr = sc.nextLine();
                    ds.sortByAlgorithm(alg, atr);
                    System.out.println("Ordenado por " + atr);
                    break;
                case 3:
                    System.out.print("Precio exacto: ");
                    int p = sc.nextInt();
                    ds.getGamesByPrice(p).forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("Precio mínimo: ");
                    int p1 = sc.nextInt();
                    System.out.print("Precio máximo: ");
                    int p2 = sc.nextInt();
                    ds.getGamesByPriceRange(p1, p2).forEach(System.out::println);
                    break;
                case 5:
                    System.out.print("Categoría: ");
                    String cat = sc.nextLine();
                    ds.getGamesByCategory(cat).forEach(System.out::println);
                    break;
                case 6:
                    System.out.print("Calidad: ");
                    int cal = sc.nextInt();
                    ds.getGamesByQuality(cal).forEach(System.out::println);
                    break;
            }
        }
        sc.close();
    }
}
