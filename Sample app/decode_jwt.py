import sys
import json
import base64

def decode_base64_url(encoded_str):
    """Adds padding and decodes a base64url string."""
    padding_needed = len(encoded_str) % 4
    if padding_needed:
        encoded_str += '=' * (4 - padding_needed)
    return base64.urlsafe_b64decode(encoded_str)

def main():
    if len(sys.argv) < 2:
        print("Usage: python decode_jwt.py <jwt>")
        return

    jwt = sys.argv[1]
    parts = jwt.split('.')

    if len(parts) >= 2:
        try:
            header = decode_base64_url(parts[0]).decode('utf-8')
            print("HEADER:")
            print(json.dumps(json.loads(header), indent=2))
        except Exception as e:
            print(f"Error decoding header: {e}")

        try:
            payload = decode_base64_url(parts[1]).decode('utf-8')
            print("\nPAYLOAD:")
            print(json.dumps(json.loads(payload), indent=2))
        except Exception as e:
            # Note: Play Integrity JWE (Encrypted JWT) payload might not be plaintext JSON
            print(f"\nPAYLOAD (Raw or Encrypted):")
            # Just print the first few bytes to see what it is
            raw_payload = decode_base64_url(parts[1])
            print(raw_payload[:100], "...")
    else:
        print("Invalid JWT format (expected at least one '.')")

if __name__ == "__main__":
    main()
