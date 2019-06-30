import java.util.*;

public class Main {

    private static Random r=new Random();

    private static Map<Integer, ArrayList<Integer>> mapOfEdges;
    private static Map<Object, Boolean> mapOfNodes;
    private static int V;
    private static int E;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        generateRandomGraph();

        if (checkGraphValidOrNot()) {
            findTheCicle();
        }
        else {
            System.out.println("NONE");
        }
    }

    //    Искать все циклы и объединять их будем одной рекурсивной процедурой:
//
//    procedure FindEulerPath (V)
//	1. перебрать все рёбра, выходящие из вершины V;
//    каждое такое ребро удаляем из графа, и
//    вызываем FindEulerPath из второго конца этого ребра;
//	2. добавляем вершину V в ответ.
//    Сложность этого алгоритма, очевидно, является линейной относительно числа рёбер.
//
//    Но этот же алгоритм мы можем записать в нерекурсивном варианте:
//
//    stack St;
//    в St кладём любую вершину (стартовая вершина);
//    пока St не пустой
//    пусть V - значение на вершине St;
//    если степень(V) = 0, то
//    добавляем V к ответу;
//    снимаем V с вершины St;
//    иначе
//    находим любое ребро, выходящее из V;
//    удаляем его из графа;
//    второй конец этого ребра кладём в St;
    private static void findTheCicle() {
        String answer="";
        Stack<Integer> stackOfV=new Stack<>();
        stackOfV.push((Integer) mapOfEdges.keySet().toArray()[0]);
        while (!stackOfV.isEmpty()) {
            Integer currentV = stackOfV.peek();
            ArrayList<Integer> ajectiveEdges = mapOfEdges.get(currentV);
            if(ajectiveEdges.size()==0) {
                answer += currentV + " ";
                stackOfV.pop();
            } else {
                Integer ajectiveV=ajectiveEdges.get(0);
                ajectiveEdges.remove(0);
                mapOfEdges.get(ajectiveV).remove(currentV);
                stackOfV.push(ajectiveV);
            }
        }
        //substring - because we dont need last==first element
        System.out.println(answer.trim().substring(0,answer.length()-2));
    }

    private static boolean checkGraphValidOrNot() {
        int countOfConnectiveComponents = countOfComponentsOfConnective(1, 0);
        if (countOfConnectiveComponents>1){
            return false;
        }
        else {
            for (Map.Entry e: mapOfEdges.entrySet()) {
                if( mapOfEdges.get(e.getKey()).size()%2!=0){
                    return false;
                }
            }
        }
        return true;
    }

    //If we have >1 componentsof connective or digit of V is not eve (x/2!=0) we cant find Eiler graph
    public static int countOfComponentsOfConnective(int currentNodeNumber, int countOfComponents){
        if(currentNodeNumber>V)
            return countOfComponents;
        if(mapOfNodes.get(currentNodeNumber))
            return countOfComponentsOfConnective(++currentNodeNumber,countOfComponents);
        //nop
        DFS(currentNodeNumber);
        countOfComponents++;
        //
        return countOfComponentsOfConnective(++currentNodeNumber,countOfComponents);
    }

    public static void DFS(Object currentNodeNumber){
        if(mapOfNodes.get(currentNodeNumber))
            return;
        mapOfNodes.replace(currentNodeNumber,true); //Because we were here
        List<Integer> edges = mapOfEdges.get(currentNodeNumber);
        for (int i = 0; i < edges.size(); i++) {
            DFS(edges.get(i)); //Recursive for all neighbors nodes
        }
    }

    public static void generateRandomGraph(){

        //System.out.println("Enter number of nodes");
        V =scanner.nextInt();
        //System.out.println("Enter number of edges; it must be <=|V|*(|V|-1)");
        E =scanner.nextInt();
        mapOfEdges= new HashMap<Integer, ArrayList<Integer>>(V);
        mapOfNodes=new HashMap<Object, Boolean>(V);


        //Add nodes in maps
        for (int i=1; i<V+1;i++){
            //False is meaning that we didn't visit this node
            mapOfNodes.put(i,false);
            mapOfEdges.put(i,new ArrayList<Integer>());
        }
        //Add edges in graph
        for(int i=0; i<E;i++){
            int startOfEdge=scanner.nextInt();
            int endOfEdge=scanner.nextInt();

            mapOfEdges.get(startOfEdge).add(endOfEdge);
            mapOfEdges.get(endOfEdge).add(startOfEdge);
        }
    }
}
