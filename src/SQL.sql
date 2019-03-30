update machadopaiaro04.Lancamentos 
set valorBruto = comissao,
    valorImposto = valor2,
    valorLiquido = valor3
where categoria_id is null;

update machadopaiaro04.Lancamentos 
set valorBruto = comissao,
    valorDespesa = valor1,
    valorLiquido = valor3
where categoria_id is not null;

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (1, 'Tela de apolices', 'TELA_APOLICE', 'Tela de apolice', 1);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (1, 'Permissao para excluir', 'APOLICE_EXCLUIR', 'Tela de apolice', 1);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Tela de apolices', 'TELA_APOLICE', 'Tela de apolice', 2);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Permissao para excluir', 'APOLICE_EXCLUIR', 'Tela de apolice', 2);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Tela de apolices', 'TELA_APOLICE', 'Tela de apolice', 3);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Permissao para excluir', 'APOLICE_EXCLUIR', 'Tela de apolice', 3);



insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (1, 'Tela de apuração', 'TELA_APURACAO', 'Tela de apuração', 1);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Tela de apuração', 'TELA_APURACAO', 'Tela de apuração', 2);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Tela de apuração', 'TELA_APURACAO', 'Tela de apoapuraçãolice', 3);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (1, 'Tela de auditoria', 'TELA_AUDITORIA', 'Tela de auditoria', 1);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Tela de auditoria', 'TELA_AUDITORIA', 'Tela de auditoria', 2);

insert into Funcionalidade (ativo, descricao, indice, pagina, perfil_id)
values (0, 'Tela de auditoria', 'TELA_AUDITORIA', 'Tela de auditoria', 3);

CREATE INDEX idx_Lancamentos_seguradora ON machadopaiaro01.Lancamentos(seguradora);
CREATE INDEX idx_Lancamentos_historico ON machadopaiaro01.Lancamentos(historico);
CREATE INDEX idx_Lancamentos_id ON machadopaiaro01.Lancamentos(id);
CREATE INDEX idx_Lancamentos_periodo ON machadopaiaro01.Lancamentos(periodo);
CREATE INDEX idx_Lan_per_hist ON machadopaiaro01.Lancamentos(periodo, historico);
CREATE INDEX idx_Lan_per_seg ON machadopaiaro01.Lancamentos(periodo, seguradora);