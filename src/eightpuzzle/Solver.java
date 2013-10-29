/*
 * Autores: Rafael Goes & Matheus Finatti
 * 
 * Classe principal responsável por executar o programa, iniciar tabuleiro, e resolver
 * 
 */

package eightpuzzle;

import java.util.ArrayList;
import java.util.Scanner;

public class Solver {
    
    //Número máximo de tentativas que será aplicado para resovler o problema
    static private int MAXIMO_TENTATIVAS = 10000;
    
    //Resolve o problema usando a heuristica 1 - Distancia de Manhattan
    static private void solveHeuristicaQtdFora (int t[][]){
        
        Tabuleiro raiz;
        Tabuleiro tabTemp;
        
        ArrayList<Tabuleiro> open = new ArrayList();
        ArrayList<Tabuleiro> closed = new ArrayList();
        
        int contador = 0;
        
        raiz = new Tabuleiro(t);
        closed.add(new Tabuleiro(t));
        open.add(raiz);
        
        //Enquanto open não vazia
        while(raiz.getPecaFora() > 0 && !open.isEmpty()){
            open.remove(raiz);

            if((tabTemp = raiz.verificaBaixo()) != null)
                insereNaOpen(tabTemp,open,closed,2);

            if((tabTemp = raiz.verificaCima()) != null)
                insereNaOpen(tabTemp,open,closed,2);

            if((tabTemp = raiz.verificaDireita()) != null)
                insereNaOpen(tabTemp,open,closed,2);

            if((tabTemp = raiz.verificaEsquerda()) != null)
                insereNaOpen(tabTemp,open,closed,2);
            
            raiz.setAberto(true);

            ordenaListaOpen(open, 1);

            if(!open.isEmpty()){
                 raiz = open.get(0);
                 closed.add(raiz);
            }
           
            contador++;

            if (contador == MAXIMO_TENTATIVAS)
                 break;
        }
        
        result(raiz, contador, 1);
        
        System.out.println("Gerando arquivos para trajetorias descartadas");
        
        int openCont = 0;
        int closedCont = 0;
        
        open.get(0).imprimeTrajetoriaOpen(0, 1);
        open.get(0).imprimeTrajetoriaClosed(0, 1);
        
        for(Tabuleiro tabTemp1: open){
            if(!tabTemp1.isAberto() && !tabTemp1.isFilhoAberto() && tabTemp1.getPecaFora() != 0){
                tabTemp1.imprimeTrajetoriaOpen(++openCont, 1);
            }
        }
        
        for(Tabuleiro tabTemp1: closed){
            if(tabTemp1.isAberto() && !tabTemp1.isFilhoAberto() && tabTemp1.getPecaFora() != 0){
                tabTemp1.imprimeTrajetoriaClosed(++closedCont, 1);
            }
        }

        System.out.println("Arquivos gerados com sucesso");
    }
    
    //Resolve o problema usando a heuristica 2 - Distancia de Manhattan
    static private void solveHeuristicaDistanciaPeca (int t[][]){
        
        Tabuleiro raiz;
        Tabuleiro tabTemp;
        
        ArrayList<Tabuleiro> open = new ArrayList();
        ArrayList<Tabuleiro> closed = new ArrayList();
        
        int contador = 0;
        
        raiz = new Tabuleiro(t);
        closed.add(new Tabuleiro(t));
        open.add(raiz);
        
        //Enquanto open não vazia
        while(raiz.getDistanciaPecas() > 0 && !open.isEmpty()){
            open.remove(raiz);

            if((tabTemp = raiz.verificaBaixo()) != null)
                insereNaOpen(tabTemp,open,closed,2);

            if((tabTemp = raiz.verificaCima()) != null)
                insereNaOpen(tabTemp,open,closed,2);

            if((tabTemp = raiz.verificaDireita()) != null)
                insereNaOpen(tabTemp,open,closed,2);

            if((tabTemp = raiz.verificaEsquerda()) != null)
                insereNaOpen(tabTemp,open,closed,2);

           ordenaListaOpen(open, 2);

           if(!open.isEmpty()){
                raiz = open.get(0);
                closed.add(raiz);
           }
           
           contador++;

           if (contador == MAXIMO_TENTATIVAS)
                break;
        }
        
        result(raiz, contador, 2);
                
        System.out.println("Gerando arquivos para trajetorias descartadas");
        
        int openCont = 0;
        int closedCont = 0;
        
        open.get(0).imprimeTrajetoriaOpen(0, 2);
        open.get(0).imprimeTrajetoriaClosed(0, 2);
        for(Tabuleiro tabTemp1: open){
            if(!tabTemp1.isAberto() && !tabTemp1.isFilhoAberto() && tabTemp1.getPecaFora() != 0){
                tabTemp1.imprimeTrajetoriaOpen(++openCont, 2);
            }
        }
        
        for(Tabuleiro tabTemp1: closed){
            if(tabTemp1.isAberto() && !tabTemp1.isFilhoAberto() && tabTemp1.getPecaFora() != 0){
                tabTemp1.imprimeTrajetoriaClosed(++closedCont, 2);
            }
        }

        System.out.println("Arquivos gerados com sucesso");
    }
    
    //Exibe o resultado da busca na tela, mostrando toda a trajetório escolhida
    static private void result(Tabuleiro raiz, int contador, int heuristica){
        if(raiz.getPecaFora() == 0){
          System.out.println("Sucesso");
          if (heuristica == 1)
              raiz.printCaminhoPecaFora();
          else
              raiz.printCaminhoDistanciaPecas();
          System.out.println("Tabuleiro foi resolvido em " + contador + " tentativas (eficiencia)\n"
                  + "Eficacia: " + raiz.getNivel() + " movimentos\n");
        }else
          System.out.println("Infelizmente nso foi possivel resolver este tabuleiro.\n"
                              +"Foram realizadas " + contador+ " tentativas");
    }
    
    //Ordena a lista open dado a lista e a heurística desejada
    static private void ordenaListaOpen(ArrayList<Tabuleiro> open, int heuristica){
        Tabuleiro aux;
        
        //Se heuristica 1 - Numero de pecas fora de lugar
        if(heuristica == 1){
            for(int i = 0; i < open.size(); i++)
                for(int j = i; j < open.size(); j++){
                    if(open.get(i).heuristicaQtdPecasFora() > open.get(j).heuristicaQtdPecasFora()){
                            aux = open.get(i);
                            open.set(i, open.get(j));
                            open.set(j, aux);
                    }
                }
        //Se heuristica 2 -  Distancia das pecas fora ate posicao
        } else {
            for(int i = 0; i < open.size(); i++)
                for(int j = i; j < open.size(); j++){
                    if(open.get(i).heuristicaDistanciaPecas()> open.get(j).heuristicaDistanciaPecas()){
                            aux = open.get(i);
                            open.set(i, open.get(j));
                            open.set(j, aux);
                    }
                }
        }
    }
    
    //Insere novo tabuleiro na open e remove da close caso necessário
    static private void insereNaOpen(Tabuleiro tabuleiroArg, ArrayList<Tabuleiro> open, ArrayList<Tabuleiro> close, int heuristica){
        
        if (heuristica == 1){
            for(Tabuleiro tabTemp: open){
                if(tabTemp.equals(tabuleiroArg)){
                    if(tabTemp.heuristicaQtdPecasFora() > tabuleiroArg.heuristicaQtdPecasFora()){
                        open.set(open.indexOf(tabTemp), tabuleiroArg);
                    }
                    return;
                }
            }

            for(Tabuleiro tabTemp: close){
                if(tabTemp.equals(tabuleiroArg)){
                    if(tabTemp.heuristicaQtdPecasFora() > tabuleiroArg.heuristicaQtdPecasFora()){
                        open.add(tabuleiroArg);
                        close.remove(tabTemp);
                    }
                    return;
                }
            }
        } else {
            for(Tabuleiro tabTemp: open){
                if(tabTemp.equals(tabuleiroArg)){
                    if(tabTemp.heuristicaDistanciaPecas() > tabuleiroArg.heuristicaDistanciaPecas()){
                        open.set(open.indexOf(tabTemp), tabuleiroArg);
                    }
                    return;
                }
            }

            for(Tabuleiro tabTemp: close){
                if(tabTemp.equals(tabuleiroArg)){
                    if(tabTemp.heuristicaDistanciaPecas() > tabuleiroArg.heuristicaDistanciaPecas()){
                        open.add(tabuleiroArg);
                        close.remove(tabTemp);
                    }
                    return;
                }
            }
        }
        
        open.add(tabuleiroArg);
    }

    //Método responsável pela execução do programa
    public static void main(String[] args) {     
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                int tabuleiro[][] = new int[3][3];

                System.out.println("+-----------------------------------------------------------------------+\n"
                                 + "| UNIVERSIDE FEDERAL DE SAO CARLOS                                      |\n"
                                 + "| 8-Puzzle Solver                                                       |\n"
                                 + "|                                                                       |\n"
                                 + "| Rafael Wolf de Goes                                                   |\n"
                                 + "| Matheus Finatti                                                       |\n"
                                 + "+-----------------------------------------------------------------------+\n");


                System.out.println("Informe o tabuleiro desejado:\n"
                        + "Exemplo: 2 1 3 <ENTER> 8 4 0 <ENTER> 7 5 6 <ENTER>");
                for(int i = 0; i < 3; i++)
                    for(int j = 0; j < 3; j++)
                        tabuleiro[i][j] = sc.nextInt();

                System.out.println("\nHeuristica 1 - Quantidade de pecas fora do lugar");
                long start = System.nanoTime();
                solveHeuristicaQtdFora(tabuleiro);
                long elapsedTime = System.nanoTime() - start;
                System.out.println("Esta solução tomou: " + elapsedTime/(1000000000.0) + " segundos");

                System.out.println("\nHeuristica 2 - Distancia de Manhattan");
                start = System.nanoTime();
                solveHeuristicaDistanciaPeca(tabuleiro);
                elapsedTime = System.nanoTime() - start;
                System.out.println("Esta solução tomou: " + elapsedTime/(1000000000.0) + " segundos");
            }
        });
    }

}
