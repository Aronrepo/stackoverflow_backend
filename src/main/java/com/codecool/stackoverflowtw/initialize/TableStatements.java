package com.codecool.stackoverflowtw.initialize;

public interface TableStatements {


    String QUESTIONS = "CREATE TABLE IF NOT EXISTS public.questions\n" +
            "(\n" +
            "    question_id serial PRIMARY KEY,\n" +
            "    title character varying(300) COLLATE pg_catalog.\"default\",\n" +
            "    description character varying(300) COLLATE pg_catalog.\"default\",\n" +
            "    created date,\n" +
            "    numberOfAnswers integer\n" +
            ")";

    String ANSWERS = "CREATE TABLE IF NOT EXISTS public.answers\n" +
            "(\n" +
            "    answer_id serial PRIMARY KEY,\n" +
            "    answer character varying(300) COLLATE pg_catalog.\"default\",\n" +
            "    question_id integer,\n" +
            "    created date,\n" +
            "    CONSTRAINT questionid FOREIGN KEY (question_id)\n" +
            "        REFERENCES public.questions (question_id) ON DELETE CASCADE\n" +
            ")";
}
