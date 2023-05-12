PORCENTAGEM_MAX = 100;
TAG_NOME = '\t'
function num_comp (x,y) {
	return x - y;
} 
function mult (a, b, m = 1, r = null) {
	//console.log(a,b,m,r)

	if(a == 0 || b == 0) {
		if(r != null)
			r.unshift(0);
		return 0;			
	}	

	

	if(Array.isArray(a)) {	
		if(r == null)
			r = [];			
		for(var c = 0; c < a.length; c++)
			mult(b, a[c], m, r);			
		
		//console.log(a,b,m,r)	
		r.sort(num_comp)	
		//console.log(r)
		return r;	
	}	

	if(Array.isArray(b))
		return mult(b, a, m, r);

	if(a == null) 
		return b;	
	if(b == null)	
		return a;
		

	var n = a * b / m;	
	if(r != null)
		r[r.length] = n;


	return n;

}

function add (a, b, f = 1) {
	if(Array.isArray(b)) {		
		r = [];
		while(r.length < b.length)
			r[r.length] = f * b[r.length];
		if(Array.isArray(a)) {
			while(r.length < a.length)
				r[r.length] = a[r.length];
			m = a.length;	
			if(m > b.length)	
				m = b.length;
			for(c = 0; c < m; c++)
				r[c] += a[c];	
				
		} else for(c = 0; c < b.length; c++)
			r[c] += a;

		r.sort(num_comp);	
		return r;	
	}

	if(Array.isArray(a)) 
		return add(b,a,f);
		
	if(b == null)
		return a;
	if(a == null)	
		return f * b;

	return a + (b * f);

}

function inv (a, m = 1) {
	if(a == null)
		return a;
	return add(m, a, -1);
}

function prob (b, d, m = 1) {

	p = m;


	for(k in d) {
		if(p == 0)
			break;
		if(d[k] != null) {
			n = b[k];
			if(!d[k])
				n = inv(n, m);
			p = mult(p, n, m);
			

		}
	}	
	return p;
}

function bacteria (nome, probabilidades = null, max_prob = PORCENTAGEM_MAX, col_sep = TAG_NOME) {
	if(probabilidades == null)
		probabilidades = {}
	else for(t in probabilidades)	
		if(Array.isArray(probabilidades[t]))	
			for(k = 0; k < probabilidades[t].length; k++)
				probabilidades[t][k] /= max_prob
		else if(probabilidades[t] != undefined && probabilidades[t] != null)
			probabilidades[t] /= max_prob
	probabilidades[col_sep] = nome	
	return probabilidades
}

function intervalo (p) {
	if(Array.isArray(p)) {
		if(p.length > 0) {
			p.sort(num_comp)
			return [p[0], p[p.length - 1]]
		}	
		return [null,null]
	}		
	return [p,p]	
}

function comparar (x, y) {
	xp = intervalo(x.probabilidade)
	yp = intervalo(y.probabilidade)

	if(xp[1] > yp[1])
		return -1;
	if(xp[1] < yp[1])	
		return 1;

	if(xp[1] === yp[1]) {
		if(xp[0] === yp[0]) {
			if(x.nome < y.nome)
				return -1;
			if(x.nome > y.nome)	
				return 1;	
		} else if(xp[0] > yp[0])
			return -1;
		else return 1;
	} 

	return 0;
}

function ranquear (bact, testes, max_prob = PORCENTAGEM_MAX, nome = TAG_NOME) {
	var resultados = []
	var param = {}	
	for(var t in testes)
		if(testes[t] == null) {
			if(testes[t] === undefined)
				continue;
			testes[t] = false
			resultados = resultados.concat(ranquear(bact, testes))

			testes[t] = true
			resultados = resultados.concat(ranquear(bact, testes))				

			testes[t] = undefined 				
		} else param[t] = testes[t]

	var res = {testes: param, resultados: []}
	resultados[resultados.length] = res	

	for(b in bact) 
		res.resultados[res.resultados.length] = {nome: bact[b][nome],  probabilidade: mult(prob(bact[b], testes), max_prob)}
	res.resultados.sort(comparar)	

	console.log(testes, param)	

	return resultados 
}