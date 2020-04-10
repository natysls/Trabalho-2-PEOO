import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {
    	Universidade.criarDB(); //cria a estrutura de arquivos caso ainda não exista.

    	Scanner scan = new Scanner(System.in);

    	while (true) { //loop infinito.
    		printMenu(); //arquivo imprime o Menu
    		int opcao = scan.nextInt();
			if (opcao == 1) {
    			cadastrarDisciplina();
    		} else if (opcao == 2) {
				printDisciplinas();
			} else if (opcao == 3) {
				cadastarGabarito();
			} else if (opcao == 4) {
				printGabaritos();
			}else if (opcao == 5) {
				gerarResultados();
			}else if (opcao == 6) {
				printResultados();
			} else if (opcao == 0) { // o Loop para aqui.
				System.exit(0); // Saindo do programa
			} else {
				System.out.println("Opção inválida");
				printMenu();
			}
    	}
    }

	//Imprime o menu
	private static void printMenu() {
		System.out.println("Digite a opção desejada:");
		System.out.println("1 - Cadastrar Disciplina");
		System.out.println("2 - Imprimir Disciplias");
		System.out.println("3 - Cadastrar Gabaritos");
		System.out.println("4 - Imprimir Gabaritos");
		System.out.println("5 - Gerar Resultados");
		System.out.println("6 - Imprimir Resultados");
		System.out.println("0 - EXIT");
	}

	//Opção 1
	private static void cadastrarDisciplina() {
		String saida = "";
		Scanner scanner = new Scanner(System.in);
		System.out.println("Digite o nome da disciplina");
		String nomeDisciplina = scanner.nextLine();
		File fileDisciplina = new File(Universidade.DB_DISCIPLINAS + nomeDisciplina + ".txt");
		
		boolean flag = true;
		while (flag) { //bandeira
			System.out.println("Digite o nome do aluno");
			String nomeAluno = scanner.nextLine().toUpperCase();
			System.out.println("Digite as respostas do aluno");
			String respostasAluno = scanner.nextLine().toUpperCase();
			if(respostasAluno.length() == 10) {
				saida += respostasAluno + "\t" + nomeAluno + "\n";
			}else {
				System.out.println("Precisam ser 10 respostas e ser V ou F");
			}
			System.out.println("Mais algum aluno? [S]");
			String continuar = scanner.nextLine();
			if (!continuar.equalsIgnoreCase("S")) {
				flag = false;
			}
		}
		Universidade.escreverArquivo(fileDisciplina, saida);
		System.out.println("Feito");
	}

	//Opção 2
	private static void printDisciplinas() {
		File arquivosDisciplinas = new File(Universidade.DB_DISCIPLINAS);
		System.out.println("Disciplinas");
		for (File disciplina: arquivosDisciplinas.listFiles() ) {
			System.out.println(); //só pra pular uma linha
			System.out.println("Nome da Disciplina: " + disciplina.getName().replace(".txt", ""));
			System.out.println(Universidade.lerArquivo(disciplina));
			System.out.println(); // só para pular uma linha
		}
	}

	//Opção 3
	private static void cadastarGabarito() {
		String saida = "";
		Scanner scanner = new Scanner(System.in);
		System.out.println("Digite o nome do gabarito");
		String nomeGabarito = scanner.nextLine();
		File fileGabarito = new File(Universidade.DB_GABARITOS + nomeGabarito + ".txt");
		
		System.out.println("Digite as respostas do gabarito");
		saida += scanner.nextLine().toUpperCase();
		if(saida.length() == 10) {
			Universidade.escreverArquivo(fileGabarito, saida);
			System.out.println("Feito");
		}else {
			System.out.println("Precisam ter 10 respostas e ser V ou F");
		}
		
	}

	//Opção 4
	private static void printGabaritos() {
		File arquivosGabaritos = new File(Universidade.DB_GABARITOS);
		System.out.println("Gabaritos");
		for (File gabarito: arquivosGabaritos.listFiles() ) {
			System.out.println(); // só pra pular uma linha
			System.out.println("Nome da Disciplina: " + gabarito.getName().replace(".txt", ""));
			System.out.println(Universidade.lerArquivo(gabarito));
			System.out.println(); // só para pular mais uma linha
		}
	}
	
	private static void gerarResultados() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Digite o nome da disciplina"); //Lendo o arquivo da disciplina
        String nomeDisciplina = scanner.nextLine();
        File fileDisciplina = new File(Universidade.DB_DISCIPLINAS + nomeDisciplina + ".txt");
        String[] lines = Universidade.lerArquivo(fileDisciplina).split("\n");

		System.out.println("Digite o nome do gabarito"); //Lendo o arquivo do gabarito
		String nomeGabarito = scanner.nextLine();
        File fileGabarito = new File(Universidade.DB_GABARITOS + nomeGabarito + ".txt");
		String gabarito = Universidade.lerArquivo(fileGabarito);

        ArrayList<Aluno> alunos = new ArrayList<Aluno>(); //lista de alunos

        for (String line: lines) {
            String nomeAluno = line.split("\t")[1];
            String respostaAluno = line.split("\t")[0];
            alunos.add(new Aluno(nomeAluno, respostaAluno));
        }
        alunos.forEach(System.out::println);// so pra testar
        
        
        double media = alunos.stream().mapToDouble( //percorre a lista e transforma em double
        		aluno -> aluno.calcularNota(gabarito)). //para cada elemento, vou usar o método
        		average().getAsDouble(); //devolve a média dos alunos

		System.out.println("Média: " + media);

		//Alunos ordenados em ordem alfabética
		List<Aluno> alunosPorNome = alunos.stream().sorted( //ordenando
				Comparator.comparing(Aluno::getNome)).collect(Collectors.toList()); //fazendo uma nova lista com essa ordem

		File fileResultadoPorNome = new File(Universidade.DB_RESULTADOS_POR_NOME + nomeDisciplina + ".txt"); //arquivo resultado ordenado por nome
		String saida = "";
		for (Aluno aluno: alunosPorNome) {
			saida += aluno.getNome() + "\t" + aluno.calcularNota(gabarito) + "\n";
		}
		saida += "Média\t" + media;
		Universidade.escreverArquivo(fileResultadoPorNome, saida);

		//Alunos ordenados em ordem decrescente de notas
		List<Aluno> alunosPorNota = alunos.stream().sorted(
				Comparator.comparing(aluno -> aluno.calcularNota(gabarito), Comparator.reverseOrder())).collect(Collectors.toList());

		File fileResultadoPorNota = new File(Universidade.DB_RESULTADOS_POR_NOTA + nomeDisciplina + ".txt"); //arquivo resultado ordenado decrescentemente por nota.
		saida = "";
		for (Aluno aluno: alunosPorNota) {
			saida += aluno.getNome() + "\t" + aluno.calcularNota(gabarito) + "\n";
		}
		saida += "Média\t" + media;
		Universidade.escreverArquivo(fileResultadoPorNota, saida);
		System.out.println("Feito, refresque a sua memória [F5]");
	}
	
	//Opção 6
	private static void printResultados() {
		File folderResultadosPorNome = new File(Universidade.DB_RESULTADOS_POR_NOME);
		System.out.println("Resultados em ordem alfabética de nome");
		for (File file: folderResultadosPorNome.listFiles()) {
			System.out.println(file.getName().replace(".txt", ""));
			System.out.println(Universidade.lerArquivo(file));
		}

		File folderResultadosPorNota = new File(Universidade.DB_RESULTADOS_POR_NOTA);
		System.out.println("Resultados em ordem decrescente de nota");
		for (File file: folderResultadosPorNota.listFiles()) {
			System.out.println(file.getName().replace(".txt", ""));
			System.out.println(Universidade.lerArquivo(file));
		}
	}



}