    package org.eclipse.jakarta.hello.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;

    @Entity
    @Table(name = "tareas")
    public class Tarea {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "titulo", nullable = false, length = 150)
        private String titulo;

        @Column(name = "completada", nullable = false)
        private Boolean completada = false;

        // Constructor vacío requerido por JPA
        public Tarea() {
        }

        // Constructor para crear tareas
        public Tarea(String titulo) {
            this.titulo = titulo;
            this.completada = false;
        }

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public Boolean getCompletada() {
            return completada;
        }

        public void setCompletada(Boolean completada) {
            this.completada = completada;
        }
    }
