import React, { useState } from 'react';
import {
  Box,
  Paper,
  Button,
  Typography,
  TextField,
  Stack,
} from '@mui/material';
import { Backspace as BackspaceIcon } from '@mui/icons-material';

interface CalculatorProps {
  onAddCustomItem: (amount: number, label: string) => void;
}

const Calculator: React.FC<CalculatorProps> = ({ onAddCustomItem }) => {
  const [display, setDisplay] = useState('0');
  const [label, setLabel] = useState('');

  const handleNumberClick = (num: string) => {
    if (display === '0') {
      setDisplay(num);
    } else {
      setDisplay(display + num);
    }
  };

  const handleClear = () => {
    setDisplay('0');
    setLabel('');
  };

  const handleBackspace = () => {
    if (display.length > 1) {
      setDisplay(display.slice(0, -1));
    } else {
      setDisplay('0');
    }
  };

  const handleAdd = () => {
    const amount = parseInt(display.replace(/,/g, ''), 10);
    if (amount > 0) {
      onAddCustomItem(amount, label || `기타 상품 (${amount.toLocaleString()}원)`);
      handleClear();
    }
  };

  const formatDisplay = (value: string) => {
    const num = parseInt(value.replace(/,/g, ''), 10);
    return isNaN(num) ? '0' : num.toLocaleString();
  };

  const formattedDisplay = formatDisplay(display);

  return (
    <Paper sx={{ p: 2 }}>
      <Typography variant="h6" sx={{ mb: 2 }}>
        계산기
      </Typography>

      <TextField
        fullWidth
        label="항목명 (선택사항)"
        value={label}
        onChange={(e) => setLabel(e.target.value)}
        sx={{ mb: 2 }}
        placeholder="예: 할인, 추가 요금 등"
      />

      <Box
        sx={{
          p: 2,
          mb: 2,
          backgroundColor: '#f5f5f5',
          borderRadius: 1,
          minHeight: 60,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'flex-end',
        }}
      >
        <Typography variant="h4" sx={{ fontWeight: 'bold' }}>
          {formattedDisplay}원
        </Typography>
      </Box>

      <Stack spacing={1}>
        <Stack direction="row" spacing={1}>
          <Button
            variant="outlined"
            fullWidth
            onClick={handleClear}
            sx={{ height: 50 }}
          >
            C
          </Button>
          <Button
            variant="outlined"
            fullWidth
            onClick={handleBackspace}
            sx={{ height: 50 }}
          >
            <BackspaceIcon />
          </Button>
        </Stack>

        <Stack direction="row" spacing={1}>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('1')}
            sx={{ height: 50 }}
          >
            1
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('2')}
            sx={{ height: 50 }}
          >
            2
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('3')}
            sx={{ height: 50 }}
          >
            3
          </Button>
        </Stack>

        <Stack direction="row" spacing={1}>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('4')}
            sx={{ height: 50 }}
          >
            4
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('5')}
            sx={{ height: 50 }}
          >
            5
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('6')}
            sx={{ height: 50 }}
          >
            6
          </Button>
        </Stack>

        <Stack direction="row" spacing={1}>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('7')}
            sx={{ height: 50 }}
          >
            7
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('8')}
            sx={{ height: 50 }}
          >
            8
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('9')}
            sx={{ height: 50 }}
          >
            9
          </Button>
        </Stack>

        <Stack direction="row" spacing={1}>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('0')}
            sx={{ height: 50 }}
          >
            0
          </Button>
          <Button
            variant="contained"
            fullWidth
            onClick={() => handleNumberClick('00')}
            sx={{ height: 50 }}
          >
            00
          </Button>
          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={handleAdd}
            sx={{ height: 50, fontSize: '1.2rem', fontWeight: 'bold' }}
          >
            추가
          </Button>
        </Stack>
      </Stack>
    </Paper>
  );
};

export default Calculator;

