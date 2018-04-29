# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import codecs
import json
import random
import re
import string


KEY_LENGTH = 19


def generate_random_string():
    res = ''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(KEY_LENGTH))
    return "-"+res


def read_file_to_list(file_name):
    text_file = codecs.open(file_name, 'r', 'utf-8')
    lines = text_file.readlines()
    artists = {}
    songs = {}
    for i in range(1, len(lines)):
        selected_substring = re.sub("[\d\.]", "", lines[i]).split(",")
        selected_substring[-1] = selected_substring[-1].rstrip()
        artist = selected_substring[0]
        song = " ".join(selected_substring[1:])
        artist_id = generate_random_string()
        artists[generate_random_string()] = {"artistId": artist_id, "artistName" : artist}
        songs[artist_id] = {generate_random_string(): {"language": "Polish", "songTitle": song}}
    text_file.close()
    return songs, artists


if __name__ == "__main__":
    filename = "/home/monikas/Desktop/studia/WPAM/songs.txt"
    songs, artists = read_file_to_list(filename)
    with open('db_data.json', 'w') as outfile:
        json.dump(artists, outfile, ensure_ascii=False, indent=4)
        json.dump(songs, outfile, ensure_ascii=False, indent=4)

