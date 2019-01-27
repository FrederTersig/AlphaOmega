package model;

public class Editore {
	
	int id;
	String nome;
	
	public Editore(int id, String nome) {
		this.id = id;
		this.nome=nome;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome=nome;
	}
	
	
}
