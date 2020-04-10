import java.io.*;
import java.util.*;

public class Universidade { //Classe responsável por gerenciar os arquivos.

    //Globais para pastas dos arquivos; db: database
    public static final String DB_DISCIPLINAS = "db/disciplinas/";
    public static final String DB_RESULTADOS = "db/resultados/";
    public static final String DB_GABARITOS = "db/gabaritos/";
    public static final String DB_RESULTADOS_POR_NOME = "db/resultados/nome/";
    public static final String DB_RESULTADOS_POR_NOTA = "db/resultados/nota/";

    
    public static void criarDB() { //cria a estrutura dos arquivos caso ainda não exista, ou seja, a database
        ArrayList<File> diretorios = new ArrayList<File>();
        diretorios.add(new File(DB_DISCIPLINAS));
        diretorios.add(new File(DB_RESULTADOS));
        diretorios.add(new File(DB_GABARITOS));
        diretorios.add(new File(DB_RESULTADOS_POR_NOME));
        diretorios.add(new File(DB_RESULTADOS_POR_NOTA));

        for (File diretorio: diretorios) {
            if (!diretorio.exists()) {
                diretorio.mkdirs(); //cria o diretório 
            }
        }
    }
    
    public static void escreverArquivo(File file, String texto) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(texto);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String lerArquivo(File file) {
        String aux = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()) { //se ele está pronto, ou seja, não vazio
                aux += bufferedReader.readLine() + "\n";
            }
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aux;
    }

    public static int calcularNota(String respostas, String gabarito) {
        int nota = 0;
        char[] resChar = respostas.toCharArray(); //vetor das respostas
        char[] gabChar = gabarito.toCharArray(); // vetor do gabarito

        boolean flag = true; // caso todas as respostas são iguais ((VVVVVVVV) ou (FFFFFFF))
        for (char caractere: resChar) {
            if (caractere != resChar[0]) {
                flag = false;
                break;
            }
        }
        if(flag){  //bandeira branca, respostas ñ são iguais
        	return 0;
        }

        for (int i = 0; i < resChar.length; i++) { //compara respostas do aluno com as do gabarito
        	if (resChar[i] == gabChar[i]) {
        		nota++;
        	}
        }
        return nota;
    }
 
}
