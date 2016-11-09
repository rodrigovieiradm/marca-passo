package br.com.guiabolso.marcapasso.controllers.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator

class HealthController implements HealthIndicator {

    private int timeout
    private String url

    HealthController(String url) {
        this(url, 5000)
    }

    HealthController(String url, int timeout) {
        this.url = url
        this.timeout = timeout
    }

    @Override
    Health health() {
        URL url = new URL(this.url)
        Health.Builder health = Health.unknown()

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection()
            conn.setRequestMethod("HEAD")
            conn.setReadTimeout(this.timeout)
            conn.setReadTimeout(this.timeout)

            int responseCode = conn.getResponseCode()

            health.withDetail("code", responseCode)

            if (responseCode == 200) {
                health.up()
            } else {
                health.down()
            }

        } catch (IOException e) {
            return health.down(e).build()
        }

        return health.build()
    }
}
