package edu.ariel.SE_project.worki.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import edu.ariel.SE_project.worki.data.LoginDataSource;
import edu.ariel.SE_project.worki.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory
{

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(LoginViewModel.class))
        {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        } else
        {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
