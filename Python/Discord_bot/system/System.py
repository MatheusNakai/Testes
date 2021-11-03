from discord.ext import commands

from  connectionSQL.ServerSQL import get_server_id, find_server_by_name, add_server
from connectionSQL.UserSQL import get_user_id, find_user_by_name, add_user
from connectionSQL.ServerDataSQL import lvl_up, exp_up, find_user_server, add_user_on_server
import sqlite3
import os

exp_p_msg = 100

def run():
    path = os.path.abspath('data_base\\meubancodedados.db')
    token = {'Your token'}
    bot_prefix = '.'
    client = commands.Bot(command_prefix=bot_prefix)
    conn = sqlite3.connect(
        path)

    @client.event
    async def on_ready():

        print("Beeloged")

    @client.event
    async def on_message(ctx):
        author_name = str(ctx.author)
        server_name = str(ctx.guild)
        if not find_user_by_name(author_name, conn):
            add_user(author_name, conn)
        if not find_server_by_name(server_name, conn):
            add_server(server_name, conn)
        author_id = get_user_id(author_name, conn)
        server_id = get_server_id(server_name, conn)
        if not find_user_server(author_id, server_id, conn):
            add_user_on_server(author_id, server_id, conn)

        exp_up(author_id, server_id, exp_p_msg, conn)

    client.run(token)