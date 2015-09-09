#populate_userpls.rb

require 'sinatra'
require 'pg'

def adduserplaylists (db_params)
		psql = PG::Connection.new(db_params)
		psql.exec_params("INSERT INTO playlists VALUES (1, 'La Traviata: Sempre Libera', 'Giuseppe Verdi')");
		psql.exec_params("INSERT INTO playlists VALUES (1, 'Turandot: Nessun Dorma', 'Giacomo Puccini')");
		psql.exec_params("INSERT INTO playlists VALUES (1, 'Rigoletto: Zitti Zitti', 'Giuseppe Verdi')");
		begin
			psql.exec_params("SELECT * FROM users WHERE username='edward'").getvalue(0,0)
		rescue ArgumentError
			psql.exec_params("INSERT INTO users VALUES ('edward', 'berliozcorgi')")
			psql.exec_params("INSERT INTO playlists VALUES (2, 'Rhapsody in Blue', 'George Gershwin')");
			psql.exec_params("INSERT INTO playlists VALUES (2, 'American in Paris', 'George Gershwin')");
			psql.exec_params("INSERT INTO playlists VALUES (2, 'Taking the A Train', 'Duke Ellington')");
		end
		begin
			psql.exec_params("SELECT * FROM users WHERE username='jane'").getvalue(0,0)
		rescue ArgumentError
			psql.exec_params("INSERT INTO users VALUES ('jane', 'pandasrule')")
			psql.exec_params("INSERT INTO playlists VALUES (3, 'White Horse', 'Taylor Swift')");
			psql.exec_params("INSERT INTO playlists VALUES (3, 'Fearless', 'Taylor Swift')");
		end
		begin
			psql.exec_params("SELECT * FROM users WHERE username='paul'").getvalue(0,0)
		rescue ArgumentError
			psql.exec_params("INSERT INTO users VALUES ('paul', 'correcthorsebatterystaple')")
			psql.exec_params("INSERT INTO playlists VALUES (4, 'Goldberg Variations', 'Glenn Gould')");
			psql.exec_params("INSERT INTO playlists VALUES (4, 'Sinfonia in D Minor', 'Arcangelo Corelli')");
			psql.exec_params("INSERT INTO playlists VALUES (4, 'Opus 2: Sonata da Camera', 'Arcangelo Corelli')");
		end
		begin
			psql.exec_params("SELECT * FROM users WHERE username='alice'").getvalue(0,0)
		rescue ArgumentError
			psql.exec_params("INSERT INTO users VALUES ('alice', 'monica4melrose')")
			psql.exec_params("INSERT INTO playlists VALUES (5, 'Strawberry Fields Forever', 'The Beatles')");
			psql.exec_params("INSERT INTO playlists VALUES (5, 'Lucy in the Sky with Diamonds', 'The Beatles')");
			psql.exec_params("INSERT INTO playlists VALUES (5, 'Praise the Lord and Pass the Amunition', 'Traditional')");
		end
		psql.close();
end		