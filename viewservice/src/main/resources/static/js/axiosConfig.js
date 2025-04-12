// src/main/resources/static/js/axiosConfig.js

// Initialize Axios with a global configuration
const axiosInstance = axios.create({
    headers: {
        "Content-Type": "application/json",
    },
});

function validateToken() {
    const token = localStorage.getItem("authToken");

    if (!token) {
        console.error("No token found!");
        return false; // No token present
    }

    try {
        // Decode token
        const decodedToken = jwt_decode(token);

        // Check if the token is expired (based on 'exp' claim)
        const currentTime = Date.now() / 1000; // Convert to seconds
        if (decodedToken.exp && decodedToken.exp < currentTime) {
            console.warn("Token has expired!");
            localStorage.removeItem("authToken"); // Remove expired token
            return false;
        }

        console.log("Token is valid:", decodedToken);
        return true; // Valid token
    } catch (error) {
        console.error("Invalid token format:", error);
        return false;
    }
}

function getDecodedToken() {

        const token = localStorage.getItem("authToken");

        try {
            const decodedToken = jwt_decode(token);

            if (decodedToken.exp * 1000 < Date.now()) {
                console.warn("Token has expired");
                localStorage.removeItem("authToken"); // Remove expired token
                return null;
            }

            return decodedToken;
        } catch (error) {
            console.error("Failed to decode JWT:", error);
            return null; // Handle invalid or corrupted tokens
        }
}

// Intercept requests to attach Authorization token
axiosInstance.interceptors.request.use((config) => {
    const token = localStorage.getItem("authToken");
    if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

// Export the configured Axios instance
export { validateToken, getDecodedToken }; // Named exports
export default axiosInstance;
