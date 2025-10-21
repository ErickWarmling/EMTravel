package com.example.emtravel.ui.cadastroUsuario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Usuario;
import com.example.emtravel.service.UsuarioRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public void salvarUsuario(String nome, String email, String dataNascimento, String telefone, String senha) {
        String camposVazios = validarCampos(nome, email, dataNascimento, telefone, senha);
        if (camposVazios != null) {
            statusCadastro.setValue(camposVazios);
            return;
        }

        if (!validarData(dataNascimento)) {
            statusCadastro.setValue("Data inválida. Use o formato DD/MM/YYYY");
            return;
        }

        Usuario usuario = new Usuario(null, nome, email, dataNascimento, telefone);

        LiveData<String> resultadoCadastro = usuarioRepository.cadastrarUsuario(usuario, senha);
        statusCadastro.addSource(resultadoCadastro, status -> {
            if ("success".equals(status)) {
                statusCadastro.setValue("Cadastro realizado com sucesso!");
            } else {
                statusCadastro.setValue(status);
            }
            statusCadastro.removeSource(resultadoCadastro);
        });
    }

    private String validarCampos(String nome, String email, String data, String telefone, String senha) {
        if (nome.isEmpty() || email.isEmpty() || data.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            return "Preencha todos os campos";
        }
        return null;
    }

    public boolean validarData(String dataNasc) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(dataNasc);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}