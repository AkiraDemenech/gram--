

try:
	from numpy import array
except ModuleNotFoundError:	
	if input("Não foi possível importar o módulo numpy. Reabra o programa depois de o instalar. \nComando: 'pip install numpy'\nhttps://pypi.org/project/numpy/\n\nVocê gostaria de continuar mesmo assim? (s/n)\t").strip()[0].lower() == 'n':
		exit()


PORCENTAGEM_MAX	= 100
SEPARADOR_COL	= '\t'
SEPARADOR_VAL 	= ', '

VARIA = 'v'
INDEFINIDOS = {VARIA.title(): None}

class bacteria:
	def __init__ (self, nome, probabilidades = None, max_prob = PORCENTAGEM_MAX, col_sep = SEPARADOR_COL):
		
		if type(nome) == bytes:
			nome = nome.decode()

		self.probabilidades = probabilidades 		 
		self.nome =	nome
		self.max =	max_prob
		self.sep =	col_sep

		self.resultado = None	
		self.zeros = 0

	def __str__ (self):#, labels = None, separator = None):
		'''if labels == None:
			labels = self.testes				
		if separator == None:
			separator = self.sep	
		s = self.nome 	
		for t in labels:	
			s += separator + str(self.probabilidades[t])
		return s'''	
		return self.__repr__() 

	def __lt__ (self, outra):
		s = self.resultado
		if hasattr(s,'__len__'):
			s = self.resultado[len(self.resultado)-1]
		if type(outra) == self.__class__: 	
			o = outra.resultado															 
			if hasattr(o,'__len__'):
				o = o[len(o)-1]	
			if o == s:				
				if self.zeros == outra.zeros:	
					if len(outra) > self.__len__():	
						return True
					if len(outra) < self.__len__():	
						return False				
					outra = outra.nome	
				else:	
					return self.zeros > outra.zeros
			elif o == None:
				return False
			else:					
				return s == None or s < o					 

		if type(outra) == str:			
			return self.nome < outra 					
		if hasattr(outra,'__len__'):
			outra = outra[len(outra)-1]	
		return outra != None and (s == None or s < outra)				

	def __gt__ (self, outra):	
		s = self.resultado
		if hasattr(s,'__len__'):
			s = s[len(s) - 1]
		if type(outra) == self.__class__: 	
			o = outra.resultado			
			if hasattr(o,'__len__'): 
				o = o[len(o) - 1]											
			if o == s:				
				if self.zeros == outra.zeros:	
					if len(outra) < self.__len__():	
						return True
					if len(outra) > self.__len__():	
						return False					
					outra = outra.nome
				else:
					return outra.zeros > self.zeros
			elif o == None:
				return True
			else:					
				return s != None and s > o 				  

		
		if type(outra) == str:			
			return self.nome > outra 					
		if hasattr(outra,'__len__'):
			outra = outra[len(outra)-1]					 
				
		return outra == None or (s != None and s > outra)		

	def __ge__ (self, outra):
		return not self.__lt__(outra)
	def __le__ (self, outra):
		return not self.__gt__(outra)	

	def __ne__ (self, outra):	
		return self.__gt__(outra) or self.__lt__(outra)
	def __eq__ (self, outra):	
		return not self.__ne__(outra)

					   				
	def __iter__ (self): 	
		return self.probabilidades.__iter__()

	def __repr__ (self):
		return self.__class__.__name__ + '(' + self.nome.__repr__() + SEPARADOR_VAL + self.probabilidades.__str__() + SEPARADOR_VAL + str(self.max) + SEPARADOR_VAL + repr(self.sep) + ')'		

	def __hash__ (self):
		return self.nome.__hash__() + 31 * self.max.__hash__()

	def __len__ (self):	
		return self.probabilidades.__len__()
	def __contains__ (self, teste):	
		return self.probabilidades.__contains__(teste)
	def __getitem__	(self, teste):
		try:
			return self.probabilidades.__getitem__(teste)/self.max
		except TypeError:	
			if self.get(teste) != None:							
				return array(self.probabilidades[teste])/self.max
			#	return [t/self.max for t in self.probabilidades[teste]]
			return None
		except KeyError:
			return	
	def __setitem__	(self, teste, resultado):
		if resultado != None:
			resultado *= self.max
		self.probabilidades.__setitem__(teste,resultado)

	def setdefault (self, resultado):	
		self.probabilidades.setdefault(resultado)
	def get (self, teste):	
		return self.probabilidades.get(teste)
	
		

	def keys (self):	
		return self.probabilidades.keys()
	def copy (self):	
		c = bacteria(self.nome,self.probabilidades.copy(),self.max,self.sep)
		c.resultado	= self.resultado
	#	c.testes	= self.testes		
		return c
	

	def probabilities (self):
		return self.probabilidades		
	def name (self):
		return self.nome
	'''def tests (self):	
		return self.testes'''	
	def result (self):	
		return self.resultado
	
	def testar (self, resultados, p = 1):					
		r = {}
		z = 0
		if p == None:
			p = self.max
		for t in self.probabilidades:							
			try:									
				if resultados[t] != None:
					if resultados[t]:
						p *= self[t] 
						r[t] = self.probabilidades[t]
					else:	
						p *= 1 - self[t]  												
						try:
							r[t] = self.max - self.probabilidades[t]						
						except TypeError:	
							r[t] = self.max - array(self.probabilidades[t])							
							r[t].sort()
							p.sort()
					b = self[t] == (not resultados[t])
					if hasattr(b,'__getitem__'):
						for a in b:
							z += a
					#	b = b.any()
					else:
						z += b	



							
			except TypeError:							
				pass # se o valor da probabilidade não for multiplicável							 
			except KeyError: 											
				continue # ou se não foi realizado esse teste 
			'''	if self[t] != None:
					n = self[t]
					t = 0
					while t < len(n):
						n[t] *= p
						t += 1
					p = n	
					p.sort() '''
					

		r = bacteria(self.nome,r,self.max,self.sep)
	#	r.testes = len(r.probabilidades)
		r.resultado = p
		r.zeros = z
		

		return r	




def testar (testes,bact,repetir_incerteza = True,certeza = None):
	
	if repetir_incerteza:		
		for t in testes:
			if testes[t] == None:				  

								

				testes = testes.copy()
				testes[t] = False
				yield from testar(testes,bact,True,certeza)

				testes = testes.copy()
				testes[t] = True
				yield from testar(testes,bact,True,certeza)

				testes = testes.copy()
				testes.pop(t)
				yield from testar(testes,bact,True,certeza) 
				return

	possibilidades = [b.testar(testes,certeza) for b in bact]
	possibilidades.sort()
	possibilidades.reverse()
	yield possibilidades, testes

	

def tabela (bact, testes = None, col_sep = None, ln_sep = '\n', var = VARIA):					
	if type(bact) == bacteria:
		b = ''
		if col_sep == None:
			col_sep = bact.sep
		if testes == None:	
			testes = bact.keys()		 				
		if hasattr(bact.resultado,'__len__') or bact.resultado != None:			 	
			b = '%s%s%d%s0%02d%s'%(str(bact.resultado),col_sep,len(bact),col_sep,bact.zeros,col_sep)			
		b += bact.nome
		for t in testes:
			b += col_sep
			p = bact.get(t)
			try:
				if p == None:
					b += var				
				else:					
					b += '%.2f' %p	
			except:	
				m = ''
				for n in p:
					b += '%s%.2f' %(m, n)	
					m = SEPARADOR_VAL			
		return b	
	l = ''	
	

	for b in bact:			
		l += tabela(b,testes,col_sep,ln_sep,var) + ln_sep
	return l	

def extrair (texto, testes = None, nomeado = True, col_sep = SEPARADOR_COL, prob_max = PORCENTAGEM_MAX, var = INDEFINIDOS): 	
	if type(texto) == str:
		p = texto
		texto = texto.split(col_sep)
		r = {}
		c = nomeado
		if type(nomeado) == str:
			c = 0
		elif nomeado > 0:
			nomeado = texto[0].strip()
		else:	
			nomeado = '<Sem nome %s>' %('@%x #%x' %(id(p),hash(p))).upper()

		while c < len(texto):	
			try:
				r[testes[c]] = eval(texto[c].title(),var)
			except IndexError:				
				break 
			except: 	
				r[testes[c]] = None 
			c += 1

		return bacteria(nomeado,r,prob_max,col_sep)	
	r = set()	
	for b in texto:
		if type(b) == str:
			if not len(b.strip()):
				continue 
			if testes == None:
				testes = b.split(col_sep) 
				c = len(testes)			
				while c:
					c -= 1
					testes[c] = testes[c].strip()				
				#	if testes.index(testes[c]) < c or not len(testes[c]):
				#		print(testes.pop(c).__repr__())
					#	c += 1
				continue
		b = extrair(b,testes,nomeado,col_sep,prob_max,var)
		if type(b) == bacteria:
			r.add(b)
		else:
			r.update(b[0])				
	return	r, corrigir(testes)	

def corrigir (lista):	
	c = len(lista)
#	print(lista)
	if type(lista) != list:
		lista = list(lista)
	while c > 0:
		c += -1
		if lista.index(lista[c]) < c or not len(lista[c]):
			lista.pop(c)
	#print(lista)		
	return lista

#	return {extrair(b,testes,nomeado,col_sep,prob_max,var) for b in texto}	







		


			







'''
b,t = extrair(open('lab.txt','r',encoding='utf-8').read().splitlines())
print(*t)
print(tabela(b,t[1:]))
print(b)
		


for r, t in testar({'a':1,'b':0,'c':None,'d':None,'e':0,'f':1},{bacteria('Coco',{'a':75,'b':25,'c':10,'d':90,'e':0,'f':100}),bacteria('Bacilo',{'a':11,'b':80,'c':20,'d':70,'e':30,'f':100})}):
	print('\n',t,'\n' + tabela(r))	

b = bacteria('Coco',{'a':75,'b':25,'c':10,'d':90,'e':0,'f':100})
print(b.resultado == None, b > -10)
'''