package com.example.emtravel.ui;

import androidx.lifecycle.MutableLiveData;

import com.example.emtravel.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UsuarioRepository {

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public UsuarioRepository() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public MutableLiveData<String> cadastrarUsuario(Usuario usuario, String senha) {
        MutableLiveData<String> statusCadastro = new MutableLiveData<>();

        auth.createUserWithEmailAndPassword(usuario.getEmail(), senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // UID gerado pelo Firebase Authentication
                        String uidUsuario = auth.getCurrentUser().getUid();
                        usuario.setUId(uidUsuario);

                        database.getReference("usuarios")
                                .child(uidUsuario)
                                .setValue(usuario)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        statusCadastro.setValue("success");
                                    } else {
                                        statusCadastro.setValue("Erro ao salvar usuário no banco de dados: " + dbTask.getException().getMessage());
                                    }
                                });
                    } else {
                        statusCadastro.setValue("Erro no cadastro: " + task.getException().getMessage());
                    }
                });
        return statusCadastro;
    }

    public MutableLiveData<String> loginUsuario(String email, String senha) {
        MutableLiveData<String> statusLogin = new MutableLiveData<>();

        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        statusLogin.setValue("success");
                    } else {
                        statusLogin.setValue("Erro no login: " + task.getException().getMessage());
                    }
                });
        return statusLogin;
    }

    public boolean usuarioLogado() {
        return auth.getCurrentUser() != null;
    }
}