package com.alexisserapio.appaerolinea

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alexisserapio.appaerolinea.databinding.ActivityFinalBinding

class FinalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinalBinding

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinalBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val info = intent.extras

        info?.let { args ->
            val name = args.getString("name","")
            val lastname = args.getString("lastname","")
            val origen = args.getString("origen","")
            val destino = args.getString("destino","")
            val dia1 = args.getString("dia1","")
            val dia2 = args.getString("dia2","")
            val mes1 = args.getString("mes1","")
            val mes2 = args.getString("mes2","")
            val ano1 = args.getString("ano1","")
            val ano2 = args.getString("ano2","")
            val hora = args.getString("hora","")
            val hora2 = args.getString("hora2","")
            val email = args.getString("email","")
            val idviajero = args.getString("idviajero","")

           // Log.d("APPSLOG","fecha recibida: $date")

            val montos = listOf(3000,4000,5000,6000,7000,8000,9000,10000)
            val montoinicial = montos.random()

            binding.TVGreeting.text = getString(R.string.greeting, name)
            binding.TVName.text = getString(R.string.complete_name, "$name $lastname")//
            binding.TV1flightDetails.text = getString(R.string.flightdetails1, origen)//
            binding.TVhoraFecha.text = getString(R.string.horayfecha, dia1,mes1,ano1,hora)//

            val letra = listOf("A","B","C","D")
            val asientosletra = letra.random()
            val asientosnumero = (1..25).random()
            val asientosletra2 = letra.random()
            val asientosnumero2 = (1..25).random()

            binding.TVAsientos.text = getString(R.string.asientos, asientosnumero,asientosletra)
            binding.TV2flightdetails.text = getString(R.string.flightdetails2, destino)//
            binding.TVHorayfechaDes.text = getString(R.string.horayfecha2, dia2,mes2,ano2,hora2)//
            binding.TVAsientos2.text = getString(R.string.asientos2, asientosnumero2,asientosletra2)
            binding.TVMonto.text = getString(R.string.monto, "$$montoinicial")

            var monto_des: Any? = null
            if(idviajero.toString().length>1){
                monto_des = montoinicial - (montoinicial*0.15)
                binding.TVMontoDes.text = getString(R.string.monto_des_ad, "15%")
            }else {
                monto_des = montoinicial
                binding.TVMontoDes.text = getString(R.string.monto_des_ad, "0%")
            }

            binding.TVMontoFinal.text = getString(R.string.monto_final, monto_des)
            binding.TVEmail.text = getString(R.string.email_aviso, email)
        }

    }
}