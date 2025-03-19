package com.alexisserapio.appaerolinea

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alexisserapio.appaerolinea.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.ETDate.setOnClickListener{showDatePickerDialog()}
        binding.ETDateBack.setOnClickListener{showDatePickerDialog2()}

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemsOrigen = listOf("Cancún","Ciudad de México","Guadalajara","Monterrey", "Los Cabos","Tijuana")
        val itemsDestino = listOf("Cancún","Ciudad de México","Guadalajara","Monterrey", "Los Cabos","Tijuana")
        val itemsHora = listOf("7:00 a.m.","10:00 a.m.","2:00 p.m.","6:00 p.m.")
        val autocompleteOrigen : AutoCompleteTextView = findViewById(R.id.dropdown_menu_origen)
        val autocompleteDestino : AutoCompleteTextView = findViewById(R.id.dropdown_menu_destino)
        val autocompleteHora : AutoCompleteTextView = findViewById(R.id.dropdown_menu_hora)
        val autocompleteHora2 : AutoCompleteTextView = findViewById(R.id.dropdown_menu_hora2)
        val adapterOrigen = ArrayAdapter(this, R.layout.list_item,itemsOrigen)
        val adapterDestino = ArrayAdapter(this, R.layout.list_item,itemsDestino)
        val adapterHora = ArrayAdapter(this, R.layout.list_item,itemsHora)
        val adapterHora2 = ArrayAdapter(this, R.layout.list_item,itemsHora)
        autocompleteOrigen.setAdapter(adapterOrigen)
        autocompleteDestino.setAdapter(adapterDestino)
        autocompleteHora.setAdapter(adapterHora)
        autocompleteHora2.setAdapter(adapterHora2)

        var OrigenSeleccionado: Any? = null
        autocompleteOrigen.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            OrigenSeleccionado = adapterView.getItemAtPosition(i)
        }

        var DestinoSeleccionado: Any? = null
        autocompleteDestino.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            DestinoSeleccionado = adapterView.getItemAtPosition(i)
        }


        with(binding){
            ETName.addTextChangedListener(loginTextWatcher)
            ETLastName.addTextChangedListener(loginTextWatcher)
            dropdownMenuOrigen.addTextChangedListener(loginTextWatcher)
            dropdownMenuDestino.addTextChangedListener(loginTextWatcher)
            dropdownMenuHora.addTextChangedListener(loginTextWatcher)
            dropdownMenuHora2.addTextChangedListener(loginTextWatcher)
            ETDate.addTextChangedListener(loginTextWatcher)
            ETDateBack.addTextChangedListener(loginTextWatcher)
            ETEmail.addTextChangedListener(loginTextWatcher)
        }

        binding.buttonVerificar.setOnClickListener {

            fun esNombreValido(nombre: String): Boolean {
                val regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s']+$".toRegex()
                return regex.matches(nombre)
            }

            if(!esNombreValido(binding.ETName.text.toString())){
                binding.ETName.error = getString(R.string.error_nombre)
                binding.ETName.requestFocus()
                return@setOnClickListener
            }
            if(!esNombreValido(binding.ETLastName.text.toString())){
                binding.ETLastName.error = getString(R.string.error_apellido)
                binding.ETLastName.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(binding.ETEmail.text.toString()).matches()){
                binding.ETEmail.error = getString(R.string.error_email)
                binding.ETEmail.requestFocus()
                return@setOnClickListener
            }

            if(OrigenSeleccionado == DestinoSeleccionado){
                binding.dropdownMenuDestino.requestFocus()
                Toast.makeText(this,getString(R.string.error_lugar),Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if(binding.ETViajerofrecuente.text.toString().length in 1..7){
                binding.ETViajerofrecuente.error= getString(R.string.error_viajerofrecuente)
                binding.ETViajerofrecuente.requestFocus()
                return@setOnClickListener
            }

            if(dia1_comp>dia2_comp&&mes1_comp==mes2_comp||mes1_comp>mes2_comp&&ano1_comp==ano2_comp||ano1_comp>ano2_comp){
                binding.ETDate.requestFocus()
                Toast.makeText(this,getString(R.string.error_date),Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val info = bundleOf(
                "name" to binding.ETName.text.trim().toString(),
                "lastname" to binding.ETLastName.text.trim().toString(),
                "origen" to OrigenSeleccionado,
                "destino" to DestinoSeleccionado,//binding.dropdownMenuDestino.text.toString(),
                "dia1" to dia1,"mes1" to mes1,"ano1" to ano1,
                "dia2" to dia2,"mes2" to mes2,"ano2" to ano2,
                "hora" to binding.dropdownMenuHora.text.toString(),
                "hora2" to binding.dropdownMenuHora2.text.toString(),
                "email" to binding.ETEmail.text.trim().toString(),
                "idviajero" to binding.ETViajerofrecuente.text.toString()
            )

            val intent = Intent(this, FinalActivity::class.java)

            intent.putExtras(info)

            startActivity(intent)


        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment({day, month, year -> AlSeleccionarFecha(day, month, year)})
        datePicker.show(supportFragmentManager,"datePicker")
    }


    private fun showDatePickerDialog2() {
        val datePicker2 = DatePickerFragment2({day, month, year -> AlSeleccionarFecha2(day, month, year)})
        datePicker2.show(supportFragmentManager,"datePicker2")
    }

    var dia1: Any? = null
    var mes1: Any? = null
    var ano1: Any? = null
    var dia1_comp: Int = 0
    var mes1_comp: Int = 0
    var ano1_comp: Int = 0
    fun AlSeleccionarFecha(day:Int, month:Int, year:Int){
        dia1 = String.format("%02d/", day)
        dia1_comp = day
        mes1= String.format("%02d/", month+1)
        mes1_comp=month+1
        ano1 = String.format("%04d", year)
        ano1_comp=year
        binding.ETDate.setText(getString(R.string.set_date, day,month+1,year))//Regresarías el día: $day/$month/$year
    }


    var dia2: Any? = null
    var mes2: Any? = null
    var ano2: Any? = null
    var dia2_comp: Int = 0
    var mes2_comp: Int = 0
    var ano2_comp: Int = 0
    fun AlSeleccionarFecha2(day:Int, month:Int, year:Int){
        dia2 = String.format("%02d/", day)
        dia2_comp = day
        mes2= String.format("%02d/", month+1)
        mes2_comp = month+1
        ano2 = String.format("%04d", year)
        ano2_comp = year
        binding.ETDateBack.setText(getString(R.string.set_dateb, day,month+1,year))//binding.ETDateBack.setText(getString(R.string.set_dateb, day+1,month,year))
    }

    private val loginTextWatcher=object:TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.buttonVerificar.isEnabled=
                binding.ETName.text.toString().trim().isNotEmpty()&&
                            binding.ETLastName.text.toString().isNotEmpty()&&
                            binding.dropdownMenuOrigen.text.toString().isNotEmpty()&&
                            binding.dropdownMenuDestino.text.toString().isNotEmpty()&&
                            binding.dropdownMenuHora.text.toString().isNotEmpty()&&
                            binding.dropdownMenuHora2.text.toString().isNotEmpty()&&
                            binding.ETDate.text.toString().isNotEmpty()&&
                            binding.ETDateBack.text.toString().isNotEmpty()&&
                            binding.ETEmail.text.toString().isNotEmpty()

        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

}

