# arquivo com as probabilidades das bactérias para positivo nos testes
PROBABILIDADES = 'lab.txt'	# Arquivo onde estão registrados os dados das bactérias e as probabilidades de positivo nos testes (com seus nomes)
PARCIAL = 'lab.log'	# Arquivo onde serão registrados os dados no formato do Python
NOME = 'lab.programa'	# Nome padrão da janela do programa
UTF8 = 'utf-8'

RESULTADO = 'res.txt'
COLUNAS_POR_LINHA = 4 # largura do teclado de entrada

try:
	import bac, tkinter, time, random
except ModuleNotFoundError as modnot:
	print("É preciso ter as bibliotecas 'tkinter', 'time' e 'random' instaladas.\nO programa também deve ter acesso ao módulo 'bac' (deixe-o na mesma pasta que 'lab')")	
	raise modnot



class programa:

	def mainloop (self):
		print('Iniciando a interface gráfica\n')
		return self.principal.mainloop()

	def destroy (self):	
		return self.principal.destroy()

	def quit (self):
		return self.principal.quit()

	def title (self, nome):
		return self.principal.title(nome)	

	def limpar (self, recado = bac.SEPARADOR_COL, aviso = 'Limpando os resultados...'):
		print(aviso)	
		self.janela.resultados.nome.config(text=aviso)		
		for res in self.janela.resultados.possibilidades:
			res.destroy()
		self.janela.resultados.possibilidades.clear()	
		self.possibilidades.clear()	
		if len(self.possibilidades):
			print(self.possibilidades,'não está vazio!')
		self.janela.resultados.nome.config(text=recado)	
		self.principal.tela.yview_moveto(0)
		self.principal.tela.refresh()
			

	def entrar (self):	
		self.limpar('Calculando....')
		self.janela.resultados.possibilidades.append(tkinter.Button(self.janela.resultados, command=self.limpar, text='Limpar resultados'))
		self.janela.resultados.possibilidades[len(self.possibilidades) - 1].pack()
		p = tkinter.Button(self.janela.resultados, command = self.limpar, text='Apagar todos os resultados')
		p.pack(side=tkinter.BOTTOM, fill=tkinter.X)
		self.janela.resultados.possibilidades.append(p)

		try:
			for pt in bac.testar(self.entradas(),self.probabilidades):
			
				p = tkinter.Frame(self.janela.resultados)
				p.pack(fill=tkinter.X, padx=14, pady=6)
							
				self.possibilidades.append(pt)
				self.janela.resultados.possibilidades.append(p)						

				q = tkinter.Frame(p)
				q.pack(fill=tkinter.X)
				tkinter.Button(q, text='Salvar dados [%d]' %len(self.possibilidades),command=lambda b=pt[0],r=pt[1],t=self.testes,s = '%d-%d-%d_%d-%d-%d[%d].TXT' %(*time.localtime()[:6],len(self.possibilidades)):print('salvando',s,registrar_tabela(b,t,r,s))).pack(side=tkinter.LEFT)
				tkinter.Button(q, text='[excluir]',command=lambda f = p.pack_forget: f()).pack(side=tkinter.RIGHT)

				tabelar(pt[1],q)
				listar(pt[0],p)
		except NameError as n:	
			tkinter.Label(self.janela, text = 'Não foi possível calcular. \nProblema interno grave: %s\nVerifique a instalação da biblioteca NumPy' %(bac.SEPARADOR_COL + str(n))).pack()			
		else:	
			self.janela.resultados.nome.config(text='%d RESULTADO%s:' %(len(self.possibilidades),'S'*(len(self.possibilidades) != 1)))	
			p = tkinter.Label(self.janela.resultados, text='%sim dos resultados %s :~)' %(['Upa, parece que nos encontramos no f','F'][random.random() > .5],bac.SEPARADOR_COL))
			self.janela.resultados.possibilidades.append(p)
			p.pack(pady=12)
		
		self.principal.tela.refresh()

	def entradas (self):	
		e = {}
		for b in self.janela.entradas.testes:
			self.janela.entradas.testes[b].botao.resultado(e)			
		print(e)	
		return e	

	def __init__ (self, raiz = None, fonte = PROBABILIDADES, parcial = PARCIAL, nome = NOME):		
		if raiz == None: # a raiz padrão é criada
			raiz = tkinter.Tk()
		
		'''
		if nome == None: # o nome padrão é concatenado
			nome = self.__class__.__name__	
			if self.__class__.__module__ != '__main__':
				nome = self.__class__.__module__ + '.' + nome 
				'''

		if type(nome) == str:		
			raiz.title(nome)		

		self.principal	= raiz
		self.parcial	= parcial	# arquivo de registro 
		self.arquivo	= fonte # arquivo onde estão tabelados os nomes dos testes e as chances de cada bactéria dar positivo
		self.possibilidades = []	# armazenamento dos resultados calculados pelo programa
		self.probabilidades = None 	
		self.testes 	= None 	

		self.inicializar_tela()
		self.inicializar_tabela()

	def inicializar_tela (self):
		# criando a tela e a barra de rolagem
		self.principal.tela = tkinter.Canvas(self.principal, background='aquamarine')
		self.principal.tela.barra = tkinter.Scrollbar(self.principal,command=self.principal.tela.yview)
		self.principal.tela.barra.pack(side=tkinter.RIGHT,fill=tkinter.Y)
		self.principal.tela.baixo = tkinter.Scrollbar(self.principal,command=self.principal.tela.xview,orient=tkinter.HORIZONTAL)
		self.principal.tela.baixo.pack(side=tkinter.BOTTOM,fill=tkinter.X)
		self.principal.tela.pack(fill=tkinter.BOTH,expand=True)
		self.principal.tela.config(yscrollcommand=self.principal.tela.barra.set, xscrollcommand=self.principal.tela.baixo.set)
		self.principal.tela.refresh = lambda event = None: (event,self.principal.tela.update(),self.principal.tela.configure(scrollregion=self.principal.tela.bbox(tkinter.ALL)))
		self.principal.tela.bind('<Configure>',self.principal.tela.refresh)
		

		
		# criando o frame para todo o conteúdo
		self.janela = tkinter.Frame(self.principal.tela, background='red')
		self.principal.tela.create_window((0,0),window=self.janela,anchor=tkinter.NW)

		
		
	def inicializar_tabela (self):
		self.janela.tabela = tkinter.Frame(self.janela, background='pink')
		self.janela.tabela.pack(pady = 16, ipadx = 8)

		self.janela.tabela.retorno =	tkinter.Label(self.janela.tabela,text='\n\tIniciando....')
		self.janela.tabela.retorno.pack(side=tkinter.BOTTOM)

		self.janela.tabela.entrada =	tkinter.Frame(self.janela.tabela)		
		self.janela.tabela.caminho =	tkinter.Entry(self.janela.tabela.entrada)
		self.janela.tabela.confirmar =	tkinter.Button(self.janela.tabela.entrada, text = 'Extrair dados / recarregar botões', command = self.ler_tabela) 
		self.janela.tabela.confirmar.pack(side=tkinter.RIGHT)		
		self.janela.tabela.caminho.pack(fill=tkinter.BOTH,side=tkinter.LEFT,expand=True)
		self.janela.tabela.entrada.pack(fill=tkinter.X)
		self.janela.tabela.caminho.insert(0,self.arquivo)

		self.ler_tabela()

		

	def ler_tabela (self):	
		try:
			self.arquivo = self.janela.tabela.caminho.get()
			self.janela.tabela.retorno.config(text = 'Extraindo de %s' %self.arquivo.encode().__str__()[1:])

			self.probabilidades, self.testes = ler_tabela(self.arquivo)
			
			if type(self.probabilidades) != set:
				self.janela.tabela.retorno.config(text='Problema na obtenção das probabilidades.')
			elif type(self.testes) != list:
				self.janela.tabela.retorno.config(text='Problema na obtenção dos testes.')
			else:	
				self.janela.tabela.retorno.config(text=self.arquivo + ' lido com sucesso')			
			self.inicializar_entradas()
			self.preencher_entradas()
			print('Testes:',bac.SEPARADOR_COL,self.testes)	
			print('\nPossibilidades:',bac.SEPARADOR_COL,self.probabilidades)				
			registrar_parcial(self.probabilidades,self.testes)
			print(bac.SEPARADOR_COL,'registrados em',repr(PARCIAL))
			return 

			
		except FileNotFoundError:	
			self.janela.tabela.retorno.config(text='Arquivo ' + self.arquivo.__repr__() + ' não encontrado!')
			print(repr(self.arquivo),'não foi encontrado. \nEle está na pasta correta? \nO nome de e o caminho até o arquivo estão escritos corretamente?')
		except: 	
			print('Os dados em',repr(self.arquivo),'não foram corretamente interpretados.')
			self.janela.tabela.retorno.config(text='Não foi possível extrair de ' + self.arquivo.__repr__())
		print('Verifique se o arquivo inserido está correto. ')	
			

	def inicializar_entradas (self): 		

		try:
			for u in self.janela.entradas.linhas:	
				u.destroy()
			self.janela.entradas.linhas.clear()
			for t in self.janela.entradas.testes:
				self.janela.entradas.testes[t].destroy() 				
			self.janela.entradas.testes.clear()				
			if len(self.janela.entradas.testes):
				print(self.janela.entradas.testes,'não está vazio!')

			
				
		except AttributeError:	
			self.janela.janelas = 	tkinter.Frame(self.janela)
			self.janela.entradas =	tkinter.Frame(self.janela.janelas)
			self.janela.entradas.linhas	= set()
			self.janela.entradas.testes = {}
			self.janela.entradas.entrar = tkinter.Button(self.janela.entradas,text='TESTAR',command=self.entrar)
			self.janela.entradas.entrar.pack(side=tkinter.BOTTOM)
			self.janela.entradas.pack(side=tkinter.LEFT, fill=tkinter.BOTH)						
			self.janela.janelas.pack()
			
			self.janela.resultados = tkinter.Frame(self.janela.janelas)
			self.janela.resultados.pack(fill=tkinter.BOTH,side=tkinter.RIGHT)
			self.janela.resultados.nome = tkinter.Label(self.janela.resultados)
			self.janela.resultados.nome.pack()
			self.janela.resultados.possibilidades = []
			

	def preencher_entradas (self, largura = COLUNAS_POR_LINHA):				

		c = self.janela.entradas
		for t in self.testes:	
			if not len(self.janela.entradas.testes)%largura:
				c = tkinter.Frame(self.janela.entradas)
				c.pack(fill=tkinter.X)
			#	self.janela.entradas.testes[len(self.janela.entradas.testes)] = c
				self.janela.entradas.linhas.add(c)
			self.janela.entradas.testes[t] = tkinter.Frame(c)			
			self.janela.entradas.testes[t].botao = tkinter.Button(self.janela.entradas.testes[t])			
			self.janela.entradas.testes[t].nome = tkinter.Label(self.janela.entradas.testes[t],text=t)			
			self.janela.entradas.testes[t].nome.pack()
			self.janela.entradas.testes[t].botao.pack()		
			self.janela.entradas.testes[t].pack(side=tkinter.LEFT,fill=tkinter.BOTH,expand=True,padx=5,pady=10,ipady=2,ipadx=4)

			teste(t,self.janela.entradas.testes[t].botao)

		self.principal.tela.yview_moveto(0)
		self.principal.tela.refresh()	
			
		
SINAIS = ('(-)','(+)')		
REPRESENTE = {None: bac.VARIA, False: 'NEGATIVO ', True: 'POSITIVO '}
RES = ((True,REPRESENTE[1] + SINAIS[1],'mediumaquamarine'),(False,REPRESENTE[0] + SINAIS[0],'tomato'),(None,'Incerto +/-','yellow'))
		
def teste (nome, botao): 			
	t = i = 'ignorar'	
	botao.resultado = lambda res, n = nome: print(n,bac.SEPARADOR_COL,'não será considerado.')
	botao.estados = {t:({'background':'white'}, botao.resultado)}
	troca = lambda prox,b:	(b.__setattr__('resultado',b.estados[prox][1]),b.config(**b.estados[prox][0],text=prox))
	for r, s, c in RES:
		botao.estados[s] = {'background':c, 
		'command':lambda oq=botao,para=t:troca(para,oq)}, lambda resultados,r = r, n = nome: resultados.__setitem__(n,r)
		t = s
	botao.estados[i][0]['command'] = lambda to=t,obj=botao: troca(to,obj)
	botao.config(**botao.estados[i][0],text=i)

def listar (bact, mestre, sep = ' %s ' %bac.SEPARADOR_COL, fim = '%'):
	caixa = tkinter.Frame(mestre)
	caixa.pack()	
	caixa.lista =	 tkinter.Listbox(caixa)
	caixa.barra_y =	 tkinter.Scrollbar(caixa, command = caixa.lista.yview)	
	caixa.barra_y.pack(fill=tkinter.Y, side=tkinter.LEFT)
	caixa.barra_x =	 tkinter.Scrollbar(caixa, command = caixa.lista.xview, orient = tkinter.HORIZONTAL)	
	caixa.barra_x.pack(fill=tkinter.X, side=tkinter.BOTTOM)
	caixa.lista.config(yscrollcommand = caixa.barra_y.set, xscrollcommand=caixa.barra_x.set)	
	caixa.lista.pack(side=tkinter.RIGHT)

	
	largura = 22
	for b in bact:
		positivo = None
		try:
			s = '%.1f%s' %(b.resultado,fim)
			positivo = b.resultado > 0
		except TypeError:								
			try:	
			#	print(b.resultado,'não é um número')
				s = t = ''
				for c in b.resultado:
					s += '%s%.2f%s' %(t,c,fim)
					t = bac.SEPARADOR_VAL
					if c > 0:					
						positivo = True
			except TypeError:		
				s = bac.VARIA

		s += sep + str(b.nome)		
		if len(s) > largura:
			largura = len(s)

		caixa.lista.insert(tkinter.END, s)
		if positivo:
			caixa.lista.itemconfig(caixa.lista.size() - 1,bg='PowderBlue')
		if largura > 125:
			largura = 144
	caixa.lista.config(width=largura)	

	return caixa

def tabelar (testes, mestre, sinais = SINAIS, largura = COLUNAS_POR_LINHA, val_sep = bac.SEPARADOR_VAL, ln_sep = '\n'):	
	texto = sep = ''
	c = 0
	for t in testes:
		try:
			texto += sep + t + sinais[testes[t]]
			sep = val_sep + (ln_sep * (c % largura == 0))
			c += 1
		#	if c % largura == 0:
		#		sep += ln_sep  
		except IndexError:
			continue	
	tkinter.Label(mestre,justify=tkinter.LEFT,text='para %d teste%s: %s' %(c,'s'*(c!=1),texto)).pack(fill=tkinter.X)	


			
			



VAR = {bac.bacteria.__name__:bac.bacteria}

def registrar_parcial (bact,testes = None,arq = PARCIAL, caso_base=True):	
	registro = arq	
	if type(arq) == str:	
		arq = open(arq,'w',encoding=UTF8)
	if caso_base:
		print('[',file=arq)	
	v = False#testes != None	
	if testes != None:
		print(file=arq,end=str(str(testes).encode()))
		v = True
	for b in bact:
		if v:
			print(',',file=arq)			
		if type(b) == bac.bacteria: 
			print(file=arq,end=repr(str(b).encode()))
		else:	
			registrar_parcial(b,arq=arq,caso_base=False)
		v = True	
	if caso_base:
		print('\n]',file=arq)
		if type(registro) == str:
			arq.close()


def ler_parcial (arq = PARCIAL):
	registro = arq
	if type(arq) == str:	
		arq = open(arq,'r',encoding=UTF8)	
	testes = None	
	bact = set()		
#	bact = {eval(b.decode(),VAR) for b in eval(arq.read())}			 
	for b in eval(arq.read()):
		if type(b) == bytes:
			b = b.decode()
		if type(b) == str:	
			b = eval(b,VAR)
		if type(b) == bac.bacteria:	
			bact.add(b)
		else:	
			testes = b

	if type(registro) == str:
		arq.close()		
	return bact, testes	

def ler_tabela (arq = PROBABILIDADES):	
	registro = arq
	if type(arq) == str:
		arq = open(arq,'r',encoding=UTF8)
	r = bac.extrair(arq.read().splitlines())
	if type(registro) == str:
		arq.close()
	return r

def registrar_tabela (bact,testes = None,resultados = None, arq = RESULTADO, trio = REPRESENTE, sinalizar = SINAIS, separador = bac.SEPARADOR_COL):	 
	registro = arq
	if type(arq) == str:
		arq = open(arq,'w',encoding=UTF8)	

	margem = separador * (1 + (3*(resultados != None))) 
	print(file=arq,end=margem)

	if testes != None:
		for t in testes:
			s = separador
			if sinalizar != None and resultados != None and resultados.get(t) != None:				
				s = ' ' + sinalizar[resultados.get(t)] + s
			print(t,end=s,file=arq)			
		if resultados != None:
			print('\n',end=margem,file=arq)	
			for t in testes:	
				print(trio[resultados.get(t)],end=separador,file=arq)				
		print(file=arq)			
	print(bac.tabela(bact,testes,separador),file=arq)		
			
	if type(registro) == str:	
		arq.close()

	



if __name__ == '__main__':	

	programa().mainloop()		
	
#	registrar_tabela(*ler_tabela())
#	registrar_parcial(*ler_tabela())
	

	

		


