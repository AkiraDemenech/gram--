package br.uel.ccs.lab;
import java.util.List;

public interface Eukaryotes {
	// interface para receber os dados

	public List<Bacteria> populate (List<Bacteria> a);
	//	popula e retorna uma lista de bactérias e as probabilidades de dar positivo nos testes

	public String[] tests ();
	// 	nomes dos testes ordenados para organizar as tabelas e interfaces gráficas

}
