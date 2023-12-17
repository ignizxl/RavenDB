package com.example.raven.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.raven.entity.Produto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.ArrayList;
import java.util.List;
import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@OpenAPIDefinition(info = @Info(title = "Raven API", version = "1.0", description = "Raven NoSQL"))
public class ProdutoController {

    @PostMapping("/salvar/{idProduto}/{nomeProduto}")
    public Produto saveProduto(@PathVariable String idProduto, @PathVariable String nomeProduto) {

        Produto produto = new Produto(idProduto, nomeProduto);
        try {
            IDocumentSession session = RavenConfig.getStore().openSession();
            session.store(produto);
            session.saveChanges();
            return produto;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return produto;
    }

    @GetMapping("/buscar/{idProduto}")
    public List<Produto> getProduto(@PathVariable String idProduto) {

        List<Produto> produtos = new ArrayList();

        try {
            IDocumentSession session = RavenConfig.getStore().openSession();

            produtos = session.query(Produto.class)
                    .whereEquals("id", idProduto)
                    .toList();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return produtos;
    }

    @DeleteMapping("/deletar/{idProduto}")
    public String deletaProduto(@PathVariable String idProduto) {

        List<Produto> produtos = new ArrayList();
        try {
            IDocumentSession session = RavenConfig.getStore().openSession();

            produtos = session.query(Produto.class)
                    .whereEquals("id", idProduto)
                    .toList();

            System.out.println(produtos);

            if (produtos.isEmpty()){
                return "Erro ao remover!";
            }
            Produto p = produtos.get(0);
            session.delete(p);


            session.saveChanges();
            return "Removido com sucesso!";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "Erro ao remover!";
        }
    }
    
    @PutMapping("/alterar/{idProduto}/{novoNome}")
    public Produto modificarProduto(@PathVariable String idProduto, @PathVariable String novoNome) {

        List<Produto> produtos = new ArrayList();
        Produto novo = null;
        try {
            IDocumentSession session = RavenConfig.getStore().openSession();

            produtos = session.query(Produto.class)
                    .whereEquals("id", idProduto)
                    .toList();
            if (produtos.isEmpty()){
                return null;
            }
            novo = produtos.get(0);

            novo.setNome(novoNome);
            session.saveChanges();

        } catch (Exception ex) {
            System.out.println(" >>>>>>>>>> " + ex.getMessage());
        }
        return novo;
    }

    @GetMapping("listar/")
    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList();
        try {
            IDocumentSession session = RavenConfig.getStore().openSession();

            produtos = session.query(Produto.class).toList();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return produtos;
    }

    public static class RavenConfig {

        private static class DocumentStoreContainer {

            public static final IDocumentStore store
                    = new DocumentStore("http://127.0.0.1:8080", "MyDatabase");

            static {
                store.initialize();
            }
        }

        public static IDocumentStore getStore() {
            return DocumentStoreContainer.store;
        }
    }
}
