PORCENTAGEM_MAX = 100;
TAG_NOME = '\t'

function mult (a, b, m = PORCENTAGEM_MAX, r = null) {
//	console.log(a,b,m,r)

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
			
		r.sort()	
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

		r.sort();	
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

function inv (a, m = PORCENTAGEM_MAX) {
	if(a == null)
		return a;
	return add(m, a, -1);
}

function prob (b, d, m = PORCENTAGEM_MAX) {

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
	probabilidades[col_sep] = nome	
	return probabilidades
}