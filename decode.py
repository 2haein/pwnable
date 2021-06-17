import base64

envalue  = 'b2JvKDV1MGpfejNfZ3UzX2NuNTV2MGF9'
envalue_bytes = base64.b64decode(envalue )
devalue = envalue_bytes.decode('ascii')

print(devalue)