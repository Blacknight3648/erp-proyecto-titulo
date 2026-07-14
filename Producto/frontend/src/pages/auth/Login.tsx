import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import AuthLayout from './AuthLayout';
import LoginForm from './LoginForm';
import { AlertTriangle, X } from 'lucide-react';

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showError, setShowError] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const success = login(email, password);
        if (success) {
            navigate('/');
        } else {
            setShowError(true);
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

            {showError && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-foreground/60 backdrop-blur-sm">
                    <div className="bg-card rounded-2xl shadow-2xl w-full max-w-sm mx-4 overflow-hidden">
                        <div className="bg-destructive/10 px-6 pt-6 pb-4 flex items-start gap-4">
                            <div className="flex-shrink-0 w-10 h-10 rounded-full bg-destructive/15 flex items-center justify-center">
                                <AlertTriangle className="w-5 h-5 text-destructive" />
                            </div>
                            <div className="flex-1 min-w-0">
                                <h3 className="text-sm font-black text-foreground uppercase tracking-wide">
                                    Credenciales incorrectas
                                </h3>
                                <p className="mt-1 text-sm text-muted-foreground leading-relaxed">
                                    El correo electrónico o la contraseña ingresados no coinciden con ningún usuario registrado en el sistema.
                                </p>
                            </div>
                            <button
                                onClick={() => setShowError(false)}
                                className="flex-shrink-0 text-muted-foreground hover:text-foreground transition-colors"
                            >
                                <X className="w-4 h-4" />
                            </button>
                        </div>
                        <div className="px-6 py-4 bg-muted border-t border-border">
                            <p className="text-xs text-muted-foreground leading-relaxed">
                                Si no recuerdas tu usuario o contraseña, contacta al <span className="font-semibold text-foreground">administrador del sistema</span> para restablecer tu acceso.
                            </p>
                        </div>
                        <div className="px-6 py-4">
                            <button
                                onClick={() => setShowError(false)}
                                className="w-full py-2.5 bg-foreground text-background text-xs font-black rounded-xl uppercase tracking-widest hover:bg-foreground/90 transition-colors"
                            >
                                Entendido
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </AuthLayout>
    );
}
