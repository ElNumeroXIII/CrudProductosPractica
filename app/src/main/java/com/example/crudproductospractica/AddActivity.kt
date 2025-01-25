package com.example.crudproductospractica

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudproductospractica.databinding.ActivityAddBinding
import com.example.crudproductospractica.models.Articulo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var database: DatabaseReference
    private var articulo: Articulo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("tienda")
        articulo = intent.getSerializableExtra("articulo") as? Articulo

        if (articulo != null) {
            // Modo edición
            binding.etNombre.setText(articulo?.nombre)
            binding.etDescripcion.setText(articulo?.descripcion)
            binding.etPrecio.setText(articulo?.precio.toString())
            binding.btnAdd.text = "Actualizar"
        }

        setListeners()
    }

    private fun setListeners() {
        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnAdd.setOnClickListener {
            if (validarDatos()) {
                guardarArticulo()
            }
        }
    }

    private fun validarDatos(): Boolean {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precio = binding.etPrecio.text.toString().toFloatOrNull()

        if (nombre.isEmpty()) {
            binding.etNombre.error = "El nombre es obligatorio"
            return false
        }

        if (descripcion.isEmpty()) {
            binding.etDescripcion.error = "La descripción es obligatoria"
            return false
        }

        if (precio == null || precio <= 0) {
            binding.etPrecio.error = "El precio debe ser un número válido mayor a 0"
            return false
        }

        return true
    }

    private fun guardarArticulo() {
        val nombre = binding.etNombre.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val precio = binding.etPrecio.text.toString().toFloat()

        val nuevoArticulo = Articulo(nombre, descripcion, precio)

        database.child(nombre).setValue(nuevoArticulo).addOnSuccessListener {
            Toast.makeText(this, "Artículo guardado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al guardar el artículo", Toast.LENGTH_SHORT).show()
        }
    }
}
