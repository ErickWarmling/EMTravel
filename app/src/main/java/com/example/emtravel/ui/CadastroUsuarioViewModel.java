package com.example.emtravel.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroUsuarioViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository;
    private MediatorLiveData<String> statusCadastro = new MediatorLiveData<>();

    public CadastroUsuarioViewModel() {
        usuarioRepository = new UsuarioRepository();
    }

    public LiveData<String> getStatusCadastro() {
        return statusCadastro;
    }

    public void salvarUsuario(String nome, String email, String dataNasc, String telefone, String senha) {
        String camposVazios = validarCampos(nome, email, dataNasc, telefone, senha);
        if (camposVazios != null) {
            statusCadastro.setValue(camposVazios);
            return;
        }

        Date dataNascimento = converterData(dataNasc);
        if (dataNascimento == null) {
            statusCadastro.setValue("Data inválida");
        }

        Usuario usuario = new Usuario(null, nome, email, dataNascimento, telefone);
        // Observa o status do repository e transforma a mensagem para CadastroUsuarioActivity
        LiveData<String> statusRepository = usuarioRepository.cadastrarUsuario(usuario, senha);
        statusCadastro.addSource(statusRepository, status -> {
            if ("success".equals(status)) {
                statusCadastro.setValue("Cadastro realizado com sucesso!");
            } else {
                statusCadastro.setValue(status);
            }
            statusCadastro.removeSource(statusRepository);
        });
    }

    private String validarCampos(String nome, String email, String data, String telefone, String senha) {
        if (nome.isEmpty() || email.isEmpty() || data.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            return "Preencha todos os campos";
        }
        return null;
    }

    private Date converterData(String dataNasc) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dataNasc);
        } catch (ParseException e) {
            return null;
        }
    }
}