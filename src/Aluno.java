public class Aluno {

	private String nome;
	private String respostas;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRespostas() {
		return respostas;
	}

	public void setRespostas(String respostas) {
		this.respostas = respostas;
	}

	public Aluno(String nome, String respostas) {
		this.nome = nome;
		this.respostas = respostas;
	}

	public double calcularNota(String gabarito) {
		return Universidade.calcularNota(this.respostas, gabarito);
	}

	@Override
	public String toString() {
		return "Aluno{" +
				"nome='" + nome + '\'' +
				", respostas='" + respostas + '\'' +
				'}';
	}
}