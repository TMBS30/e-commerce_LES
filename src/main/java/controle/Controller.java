package controle;

import dominio.EntidadeDominio;

import java.util.List;

public class Controller extends Ctrl{
    public String salvar(EntidadeDominio entidade) throws Exception {
        return fachada.salvar(entidade);
    }

    public List<EntidadeDominio> consultar(EntidadeDominio entidade){
        return fachada.consultar(entidade);
    }

    public String alterar(EntidadeDominio entidade){
        return fachada.alterar(entidade);
    }

    public String excluir(EntidadeDominio entidade){
        return fachada.excluir(entidade);
    }

    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        return fachada.selecionar(entidade);
    }

}