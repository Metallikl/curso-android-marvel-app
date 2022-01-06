package com.example.marvelapp.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.luche.core.domain.Character
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//Indica que esse cara pode receber uma injeção de dependencia
@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    //Instancia nosso viewmodelo usando a delegate propertie by viewModels, que não tem a ver com
    // o Hilt, mas irá criar a instancia com base nas anotações que fizemos
    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var charactersAdapter : CharactersAdapter

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
        //
        observeInitialLoadState()
        //O Flow sempre deve rodar dentro de um scope de coroutine.
        //Nesse caso, lifecycleScope para chamar o viewModel e executar a chama da API
        lifecycleScope.launch {
            //Quando trabalhamos com flow, para que ele determinar que esse flow NAO deve ser
            // ouvido enquanto o app estiver em background, usamos o lifecycler owener informando
            // que somente quando o app estiver STARTED ele deve ser ouvido.
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                //Chama fun que retorno o Flow do dados e character.
                //O collect do flow, é equivalente o observe do livedata e fica ouvindo as alterações
                // enviadas pelo fluxo de dados.
                viewModel.charactersPagingData("").collect { pagingData ->
                    //Ao resgatar a lista, submete os dados ao adapter.
                    charactersAdapter.submitData(pagingData = pagingData )
                }
            }
        }
    }

    private fun initCharactersAdapter() {
        charactersAdapter = CharactersAdapter()
        with(binding.recyclerCharacter){
            scrollToPosition(0)
            setHasFixedSize(true)
            //"Concatena" ao nosso adapter um adapter que de footer, que será o
            // CharactersLoadStateAdapter que criamos utilizando o loadState do paging.
            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter(
                    charactersAdapter::retry
                )
            )
        }
    }

    private fun observeInitialLoadState(){

        lifecycleScope.launch {
            //O PagingDataAdapter, possui propertie loadStateFlow que permite observar as mudanças
            //no estado de carregamento.
            //collectLatest pega o ultimo estado
            charactersAdapter.loadStateFlow.collectLatest { loadState->
                //Baseado no loadState.refresh, definmos a visibilidade do shimmer, animação de
                // incio e pausa e retorna qual view do viewFliper deve ser exibida
                binding.flipperCharacters.displayedChild =   when(loadState.refresh){
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOAD
                    }
                    is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTER
                    }
                    is LoadState.Error -> {
                        setShimmerVisibility(false)
                        //Listener no btn de retry do layout de error
                        binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                            //Limpa o adapter e faz a requisição da posição inicial 0
                            charactersAdapter.refresh()
                        }
                        FLIPPER_CHILD_ERROR
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.apply {
            isVisible = visibility
            if(visibility){
                startShimmer()
            }else stopShimmer()
        }
    }

    companion object{
        private const val FLIPPER_CHILD_LOAD = 0
        private const val FLIPPER_CHILD_CHARACTER = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}