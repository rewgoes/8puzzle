/*
 * Autores: Rafael Goes & Matheus Finatti
 * 
 * Essa classe define o tabuleiro e alguma de suas funções como
 * calculo da heurística, troca de posição, entre outras.
 * 
 */

package eightpuzzle;

public class Tabuleiro {
    //Valores de referencia de onde veio a peça do movimento
    private static final int ESQUERDA = 0;
    private static final int DIREITA = 1;
    private static final int CIMA = 2;
    private static final int BAIXO = 3;
    
    private int tabuleiro[][] = new int[3][3];
    private int pecaFora, posicaoVertical, posicaoHorizontal, ladoVindo, distanciaPecas;

    private Tabuleiro pai;
    private int nivel;
    
    //Inicia o tabuleiro
    Tabuleiro(int t[][]){
        nivel = 0;
        ladoVindo = - 1;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++){
                tabuleiro[i][j] = t[i][j];
                if(t[i][j] == 0){
                    posicaoVertical = i;
                    posicaoHorizontal = j;
                }
            }
        
        calculaPecasFora();
        calculaDistanciaPecas();
    }
    
    //Faz o calculo da heuristica 1 - contar peças fora da posição correta
    public final void calculaPecasFora(){
        pecaFora = 0;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if(i==1){
                    if(j==0){
                        if(tabuleiro[i][j] != 8) pecaFora++;
                    }else if(j==2){
                        if(tabuleiro[i][j] != 4) pecaFora++;
                    }else
                        if(tabuleiro[i][j] != 0) ;//pecaFora++;
                }else if(i == 0){
                    if(tabuleiro[i][j] != j + 1) pecaFora++;
                }else
                    if(tabuleiro[i][j] != i * 3 + 1 - j) pecaFora++;
    }
    
    //Faz o calculo da heuristica 2 - fazer a soma da distância de Manhataan de todas as peças
    public final void calculaDistanciaPecas(){
        distanciaPecas = 0;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++){
                switch(tabuleiro[i][j]){
                    case 1:
                        distanciaPecas = distanciaPecas + i + j;
                        break;
                    case 2:
                        distanciaPecas = distanciaPecas + i + ((j - 1 > 0) ? (j-1) : -(j-1));
                        break;
                    case 3:
                        distanciaPecas = distanciaPecas + i + ((j - 2 > 0) ? (j-2) : -(j-2));
                        break;
                    case 4:
                        distanciaPecas = distanciaPecas + ((i - 1 > 0) ? (i-1) : -(i-1)) + ((j - 2 > 0) ? (j-2) : -(j-2));
                        break;
                    case 5:
                        distanciaPecas = distanciaPecas + ((i - 2 > 0) ? (i-2) : -(i-2)) + ((j - 2 > 0) ? (j-2) : -(j-2));
                        break;
                    case 6:
                        distanciaPecas = distanciaPecas + ((i - 2 > 0) ? (i-2) : -(i-2)) + ((j - 1 > 0) ? (j-1) : -(j-1));
                        break;
                    case 7:
                        distanciaPecas = distanciaPecas + ((i - 2 > 0) ? (i-2) : -(i-2)) + j;
                        break;
                    case 8:
                        distanciaPecas = distanciaPecas + ((i - 1 > 0) ? (i-1) : -(i-1)) + j;
                        break;
                }
            }
    }
    
    //Move vazio para a esquerda
    public boolean moveEsq(){
        if(posicaoHorizontal != 0){
            int aux;
            aux = tabuleiro[posicaoVertical][posicaoHorizontal - 1];
            tabuleiro[posicaoVertical][posicaoHorizontal - 1] = tabuleiro[posicaoVertical][posicaoHorizontal];
            tabuleiro[posicaoVertical][posicaoHorizontal] = aux;
            posicaoHorizontal = posicaoHorizontal - 1;
            calculaPecasFora();
            calculaDistanciaPecas();
            return true;
        }
        return false;
    }
    
    //Move vazio para a direita
    public boolean moveDir(){
        if(posicaoHorizontal != 2){
            int aux;
            aux = tabuleiro[posicaoVertical][posicaoHorizontal + 1];
            tabuleiro[posicaoVertical][posicaoHorizontal + 1] = tabuleiro[posicaoVertical][posicaoHorizontal];
            tabuleiro[posicaoVertical][posicaoHorizontal] = aux;
            posicaoHorizontal = posicaoHorizontal + 1;
            calculaPecasFora();
            calculaDistanciaPecas();
            return true;
        }
        return false;
    }
    
    //Move vazio para baixo
    public boolean moveBaixo(){
        if(posicaoVertical != 2){
            int aux;
            aux = tabuleiro[posicaoVertical + 1][posicaoHorizontal];
            tabuleiro[posicaoVertical + 1][posicaoHorizontal] = tabuleiro[posicaoVertical][posicaoHorizontal];
            tabuleiro[posicaoVertical][posicaoHorizontal] = aux;
            posicaoVertical = posicaoVertical + 1;
            calculaPecasFora();
            calculaDistanciaPecas();
            return true;
        }
        return false;
    }
    
    //Move vazio para cima
    public boolean moveCima(){
        if(posicaoVertical != 0){
            int aux;
            aux = tabuleiro[posicaoVertical - 1][posicaoHorizontal];
            tabuleiro[posicaoVertical - 1][posicaoHorizontal] = tabuleiro[posicaoVertical][posicaoHorizontal];
            tabuleiro[posicaoVertical][posicaoHorizontal] = aux;
            posicaoVertical = posicaoVertical - 1;
            calculaPecasFora();
            calculaDistanciaPecas();
            return true;
        }
        return false;
    }
    
    //Verifica se é possível ir para a esquerda
    public Tabuleiro verEsq(){
        Tabuleiro tab = new Tabuleiro(tabuleiro);
        tab.setPai(this);
        tab.setNivel(this.getNivel() + 1);
        if(tab.moveEsq() && tab.getLadoVindo() != Tabuleiro.DIREITA){
            tab.setLadoVindo(Tabuleiro.ESQUERDA);
            return tab;
        }else
            return null;
    }
    
    //Verifica se é possível ir para a direita
    public Tabuleiro verDir(){
        Tabuleiro tab = new Tabuleiro(tabuleiro);
        tab.setPai(this);
        tab.setNivel(this.getNivel() + 1);
        if(tab.moveDir()&& tab.getLadoVindo() != Tabuleiro.ESQUERDA){
            tab.setLadoVindo(Tabuleiro.DIREITA);
            return tab;
        }else
            return null;
    }
    
    //Verifica se é possível ir para baixo
    public Tabuleiro verBaixo(){
        Tabuleiro tab = new Tabuleiro(tabuleiro);
        tab.setPai(this);
        tab.setNivel(this.getNivel() + 1);
        if(tab.moveBaixo() && tab.getLadoVindo() != Tabuleiro.CIMA){
            tab.setLadoVindo(Tabuleiro.BAIXO);
            return tab;
        }else
            return null;
    }
    
    //Verifica se é possível ir para cima
    public Tabuleiro verCima(){
        Tabuleiro tab = new Tabuleiro(tabuleiro);
        tab.setPai(this);
        tab.setNivel(this.getNivel() + 1);
        if(tab.moveCima() && tab.getLadoVindo() != Tabuleiro.BAIXO){
            tab.setLadoVindo(Tabuleiro.CIMA);
            return tab;
        }else
            return null;
    }
    
    //Imprimi tabuleiro com resultado da heurística 1
    public void imprimirPecaFora(){
        System.out.println("+-----------+");
        for(int i = 0; i < 3; i++){
            System.out.print("| ");
            for(int j = 0; j < 3; j++){
                if(tabuleiro[i][j] != 0)
                    System.out.print(tabuleiro[i][j] + " | ");   
                else
                    System.out.print("  | ");
                
                 if(i == 1 && j == 2)
                        System.out.print(" Soma do numero de pecas fora do lugar: " + getPecaFora());
            }
            System.out.println("\n+-----------+");
       }
        System.out.println();
    }
    
    //Imprimi tabuleiro com resultado da heurística 2
    public void imprimirDistanciaPecas(){
        System.out.println("+-----------+");
        for(int i = 0; i < 3; i++){
            System.out.print("| ");
            for(int j = 0; j < 3; j++){
                if(tabuleiro[i][j] != 0)
                    System.out.print(tabuleiro[i][j] + " | ");   
                else
                    System.out.print("  | ");
                
                 if(i == 1 && j == 2)
                        System.out.print(" Soma distancia das pecas fora do lugar: " + getDistanciaPecas());
            }
            System.out.println("\n+-----------+");
       }
        System.out.println();
    }

    //Getters and Setters
    public int getPecaFora() {
        return pecaFora;
    }
    
    public int getDistanciaPecas() {
        return distanciaPecas;
    }
    
    public int getPosicaoCZero() {
        return posicaoHorizontal;
    }

    public void setPosicaoCZero(int posicaoCZero) {
        this.posicaoHorizontal = posicaoCZero;
    }

    public int getPosicaoLZero() {
        return posicaoVertical;
    }

    public void setPosicaoLZero(int posicaoLZero) {
        this.posicaoVertical = posicaoLZero;
    }

    public int[][] getTabuleiro() {
        return tabuleiro;
    }

    public int getLadoVindo() {
        return ladoVindo;
    }

    public void setLadoVindo(int ladoVindo) {
        this.ladoVindo = ladoVindo;
    }

    public Tabuleiro getPai() {
        return pai;
    }

    public void setPai(Tabuleiro pai) {
        this.pai = pai;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }

    public int heuristicaQtdPecasFora(){
        return pecaFora + nivel;
    }
    
    public int heuristicaDistanciaPecas(){
        return distanciaPecas + nivel;
    }
    
    public void printCaminhoPecaFora(){
        if(this.getPai() != null)
            this.getPai().printCaminhoPecaFora();
        imprimirPecaFora();
    }
    
    public void printCaminhoDistanciaPecas(){
        if(this.getPai() != null)
            this.getPai().printCaminhoDistanciaPecas();
        imprimirDistanciaPecas();
    }
    
    //Compara de duas configurações de tabuleiro são iguais
    public boolean equals(Tabuleiro p){
        int tab[][] = p.getTabuleiro();
        for(int i = 0; i < 3; i++)
            for(int j = i; j < 3; j++)
                if(tabuleiro[i][j] != tab[i][j])
                    return false;
        return true;
    }

}
