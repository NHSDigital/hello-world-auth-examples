@pytest.mark.happy_path
    def test_application_restricted_api_with_api_key(self, config, service_url):
        # Given
        endpoint = f"{service_url}/hello/application"
        api_key = config["api_key"]

        # When
        response = requests.get(endpoint, headers={"apikey": api_key})

        # Then
        assert response.status_code == 200
        assert "Hello Application" in response.text