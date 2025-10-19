package com.example.emtravel.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.emtravel.model.Usuario;

public class MainViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository;
    private LiveData<Usuario> usuarioLogado;
    private LiveData<String> primeiroNome;

    public MainViewModel() {
        usuarioRepository = new UsuarioRepository();
        usuarioLogado = getUsuarioLogado();
        primeiroNome = transformarNomeUsuario(usuarioLogado);
    }

    public LiveData<Usuario> getUsuarioLogado() {
        return usuarioRepository.getUsuarioLogado();
    }

    public LiveData<String> getPrimeiroNome() {
        return primeiroNome;
    }

    private LiveData<String> transformarNomeUsuario(LiveData<Usuario> usuarioLiveData) {
        return Transformations.map(usuarioLiveData, usuario -> {
            if (usuario == null || usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                return "Usuário";
            }

            String[] partesNome = usuario.getNome().trim().split(" ");
            return partesNome[0];
        });
    }

}
