-- liquibase formatted sql

-- changeset IrinaSerebryakova:1
CREATE TABLE notification_task (id SERIAL, chatId SERIAL, message TEXT, datetime TIMESTAMP);
ALTER TABLE notification_task ADD PRIMARY KEY (id);

-- changeset IrinaSerebryakova:2
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:10');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:11');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:12');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:13');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:14');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:15');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу','16.02.2025 12:16');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:17');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:18');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:19');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:20');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:21');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:22');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:23');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:24');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:25');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:26');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:27');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:28');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Сделать домашнюю работу', '16.02.2025 12:29');
insert INTO public.notification_task (chatid, message, datetime) VALUES (614749807, 'Ложиться спать, завтра рано вставать!', '16.02.2025 13:00');
