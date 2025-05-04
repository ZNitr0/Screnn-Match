package br.com.screenmatch.Principal;

import br.com.screenmatch.excecao.ErroDeConversaoDeAnoException;
import br.com.screenmatch.modelos.Titulo;
import br.com.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class PrincipalComBuscas {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do filme: ");
        var busca = scanner.nextLine();

        String endereco = "https://www.omdbapi.com/?t=" + busca.replace(" ","+") + "&apikey=bd4571b8";
        try {


            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println();

            String JSON = response.body();
            System.out.println(JSON);

            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            TituloOmdb MeuTituloOmdb = gson.fromJson(JSON, TituloOmdb.class);
            System.out.println(MeuTituloOmdb);

            Titulo meuTitulo = new Titulo(MeuTituloOmdb);
            System.out.println("Titulo já convertido");
            System.out.println(meuTitulo);

            FileWriter escrita = new FileWriter("Filmes.txt");
            escrita.write(meuTitulo.toString());
            escrita.close();

        } catch (NumberFormatException e) {
            System.out.println("Aconteceu um erro: ");
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Algum erro de argumento na busca, verifique o endereço");
        } catch (ErroDeConversaoDeAnoException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("O programa finalizou corretamente!");
    }

}
