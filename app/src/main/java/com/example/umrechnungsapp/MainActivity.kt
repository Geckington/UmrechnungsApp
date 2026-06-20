package com.example.umrechnungsapp

import android.os.Bundle
import android.view.View
import android.app.DatePickerDialog
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.umrechnungsapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var button: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        button = findViewById(R.id.buttonadd)
        val inputFieldFlaeche = findViewById<EditText>(R.id.inputFieldFlaeche)
        val buttonBerechnenFlaeche = findViewById<Button>(R.id.buttonBerechnenFlaeche)
        val resultFlaeche = findViewById<TextView>(R.id.resultFlaeche)
        val inputFieldGeld = findViewById<EditText>(R.id.inputFieldGeld)
        val buttonBerechnenGeld = findViewById<Button>(R.id.buttonBerechnenGeld)
        val resultGeld = findViewById<TextView>(R.id.resultGeld)
        val buttonAlterWaehlen = findViewById<Button>(R.id.buttonAlterWaehlen)
        val resultAlter = findViewById<TextView>(R.id.resultAlter)




        //Button für die Flächenberechnung. Falls keine Eingabe bzw. eine ungülige
        //Angabe gemacht wird wird der Nutzer informiert. Es wird auf die 4. Dezimalstelle gerundet.
        buttonBerechnenFlaeche.setOnClickListener {
            val input = inputFieldFlaeche.text.toString()
            if (input.isBlank()) {
                resultFlaeche.text = "Bitte einen Wert eingeben."
                return@setOnClickListener
            }
            val qm = input.toDoubleOrNull()
            if (qm == null || qm < 0) {
                resultFlaeche.text = "Ungültige Eingabe."
                return@setOnClickListener
            }
            val felder = qm / 7140.0
            resultFlaeche.text = "%.4f Fußballfelder".format(felder)
        }

        //Button für die Geld zu Zeit Berechnung. Der Eurowert
        //wird einfach multipliziert und in Jahren und Tagen wiedergegeben.
        buttonBerechnenGeld.setOnClickListener {
            val input = inputFieldGeld.text.toString()
            if (input.isBlank()) {
                resultGeld.text = "Bitte einen Wert eingeben."
                return@setOnClickListener
            }
            val euro = input.toLongOrNull()
            if (euro == null || euro < 0) {
                resultGeld.text = "Ungültige Eingabe (ganze Zahl eingeben)."
                return@setOnClickListener
            }
            val years = euro / (60 * 60 * 24 * 365.25)
            val days = euro / (60 * 60 * 24)

            resultGeld.text = "$euro € = ${"%.2f".format(years)} Jahre bzw. $days Tag(e)"
        }


        //Button für den Kalender. Es wird ein Kalender erstellt in dem abgespeichert wird, wann man geboren wurde. Das Datum wird
        //mit dem Datum heute berechnet und geschaut, wieviel Jahre und Tage dazwischen liegen.
        buttonAlterWaehlen.setOnClickListener {
            val heute = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, jahr, monat, tag ->
                    val geburtsdatum = LocalDate.of(jahr, monat + 1, tag)
                    val heuteLocal = LocalDate.now()

                    val period = Period.between(geburtsdatum, heuteLocal)
                    val tageGesamt = ChronoUnit.DAYS.between(geburtsdatum, heuteLocal)

                    val jahre = period.years
                    val monate = period.months
                    val tage = period.days

                    resultAlter.text =
                        "Alter: $jahre Jahre, $monate Monate, $tage Tage\n" + "Insgesamt: $tageGesamt Tage"
                },
                heute.get(Calendar.YEAR),
                heute.get(Calendar.MONTH),
                heute.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.maxDate = heute.timeInMillis
            datePicker.show()
        }

        //Hier wird eingestellt, welcher Umrechner verfügbar ist. Wenn man einen anklickt, wird dieser Visible. Ansonsten sind alle
        //auf gone gestellt.
        button.setOnClickListener {

            inputFieldFlaeche.visibility = View.GONE
            buttonBerechnenFlaeche.visibility = View.GONE
            resultFlaeche.visibility = View.GONE

            inputFieldGeld.visibility = View.GONE
            buttonBerechnenGeld.visibility = View.GONE
            resultGeld.visibility = View.GONE

            buttonAlterWaehlen.visibility = View.GONE
            resultAlter.visibility = View.GONE

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Was möchten Sie umrechnen?")

            dialog.setPositiveButton("Flächenumrechnung") { _, _ ->
                inputFieldFlaeche.visibility = View.VISIBLE
                buttonBerechnenFlaeche.visibility = View.VISIBLE
                resultFlaeche.visibility = View.VISIBLE
            }

            dialog.setNeutralButton("Alter") { _, _ ->
                buttonAlterWaehlen.visibility = View.VISIBLE
                resultAlter.visibility = View.VISIBLE
            }

            dialog.setNegativeButton("Geld") { _, _ ->
                inputFieldGeld.visibility = View.VISIBLE
                buttonBerechnenGeld.visibility = View.VISIBLE
                resultGeld.visibility = View.VISIBLE
            }

            dialog.show()
        }
    }
}









