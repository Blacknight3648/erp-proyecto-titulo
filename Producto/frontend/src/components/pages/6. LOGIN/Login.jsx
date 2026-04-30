import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../../contexts/AuthContext';
import AuthLayout from '../../ui/auth/AuthLayout';
import LoginForm from '../../ui/auth/LoginForm';

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const success = login(email, password);
        if (success) {
            navigate('/');
        } else {
            alert('Credenciales incorrectas (Usa admin / admin)');
        }
    };

    return (
        <AuthLayout>
            <LoginForm
                email={email}
                setEmail={setEmail}
                password={password}
                setPassword={setPassword}
                onSubmit={handleSubmit}
            />
        </AuthLayout>
    );
}
