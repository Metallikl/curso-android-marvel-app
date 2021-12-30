package com.example.marvelapp.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.luche.core.domain.Character
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//Indica que esse cara pode receber uma injeção de dependencia
@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    //Instancia nosso viewmodelo usando a delegate propertie by viewModels, que não tem a ver com
    // o Hilt, mas irá criar a instancia com base nas anotações que fizemos
    private val viewModel: CharactersViewModel by viewModels()

    private val charactersAdapter = CharactersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCharactersBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCharactersAdapter()
        //O Flow sempre deve rodar dentro de um scope de coroutine.
        //Nesse caso, lifecycleScope para chamar o viewModel e executar a chama da API
        lifecycleScope.launch {
            //Chama fun que retorno o Flow do dados e character.
            //O collect do flow, é equivalente o observe do livedata e fica ouvindo as alterações
            // enviadas pelo fluxo de dados.
            viewModel.charactersPagingData("").collect { pagingData ->
                //Ao resgatar a lista, submete os dados ao adapter.
                charactersAdapter.submitData(pagingData = pagingData )
            }
        }
    }

    private fun initCharactersAdapter() {
        with(binding.recyclerCharacter){
            setHasFixedSize(true)
            adapter = charactersAdapter
        }
    }
}