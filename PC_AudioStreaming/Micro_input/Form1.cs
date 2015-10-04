using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.IO;

// NAudio c# API 
using NAudio.Wave;
using System.Runtime.InteropServices;
using System.Threading;

namespace Micro_input
{
    
    // 전화 왔을 때 사용되는, 알림 관련 UI Class
    public partial class Form1 : Form
    {

        // 이하 환경 변수
        private const int MarginX = 50; // x좌표 여백
        private const int MarginY = 50; // y좌표 여백
        private const int DeleayTime = 500; // 창이 나타나는 시간 (밀리초 단위)
        private const int Flag = Animate.DwFlagBlend; // 애니메이션 효과
        // 이상 환경 변수

        JhjProvider jhj = new JhjProvider(new WaveFormat(8000,16,1));

        WaveIn wi; // Aduio input value
        BufferedWaveProvider bwp; // Audio buffer : 
        BufferedWaveProvider bwp2;
        IWavePlayer wo = new WaveOut();

        private const int ServerPortNumber = 24700;
        Socket udpSocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
        EndPoint localEP = new IPEndPoint(IPAddress.Any, ServerPortNumber);
        EndPoint remoteEP = new IPEndPoint(IPAddress.None, ServerPortNumber);

        byte[] sendBuffer = new byte[2400];
        byte[] receiveBuffer = new byte[2400];


        // Main
        public Form1()
        {
            InitializeComponent();

            jhj.ToString();


            //UDP 통신 : 음성스트리밍의 속도를 위하여
            udpSocket.Bind(localEP); // 준비단계
            Console.WriteLine("(before) remote IP - {0}", remoteEP.ToString());
            Console.WriteLine("UDP 서버 대기 중");

        
            //UDP Audio streaming data를 받음
            //Audio Streaming(Recieve & Send) Part
            int receivedSize = udpSocket.ReceiveFrom(sendBuffer, ref remoteEP);
            Encoding.UTF8.GetString(sendBuffer, 0, receivedSize);
            Console.WriteLine("recv string - {0} from {1}", sendBuffer, remoteEP);


            //전화왔을 때, 벨소리 울리는 부분
            Console.WriteLine("thread 시작");
            Thread thr = new Thread(new ThreadStart(PlayMP3));
            thr.Start();

            //전화 알림 애니메이션
            AnimateWindow();

        }


        private void button1_Click(object sender, EventArgs e)
        {
            waveOut.Pause();
            waveOut.Dispose();

            wi = new WaveIn();
            wi.DataAvailable += wi_DataAvailable;

            //Audio Streaming Send를 위한 provider
            bwp = new BufferedWaveProvider(wi.WaveFormat);
            bwp.DiscardOnBufferOverflow = true;

            //Audio Streaming Receive를 위한 provider
            bwp2 = new BufferedWaveProvider(new WaveFormat(8000, 16, 1)); 
            bwp2.DiscardOnBufferOverflow = true;
            HideAnimateWindow(); // 전화알림 관련 함수

            //Sender를 위한 변수 init & start
            wo.Init(bwp2); 
            wo.Play();

            udpSocket.SendTo(Encoding.UTF8.GetBytes("RECOGNITION_CALL_START_FROM_SERVER"), remoteEP);
            Console.WriteLine("Send Start Mesg");
      
            //노트북(PC)의 마이크로부터 입력받은 Aduio data를 입력받는 부분(NAudio 지원)
            wi.StartRecording();
            

            Thread thr = new Thread(new ThreadStart(Listen));
            thr.Start();
        }

        // 마이크로 입력받은 Audio를 Send하는 부분
        void wi_DataAvailable(object sender, WaveInEventArgs e)
        {
            Console.WriteLine("available : {0} bytes", e.BytesRecorded);            
            udpSocket.SendTo(e.Buffer, remoteEP);
            bwp.AddSamples(e.Buffer, 0, e.BytesRecorded);
        }
        
        //통화상대로부터 받은 데이터를 받는 부분
        private void Listen()
        {         
            int receivedSize;            
            while (true)
            {
                // 통화 상대로부터 받은 Audio data를 수신
                receivedSize = udpSocket.ReceiveFrom(receiveBuffer, ref remoteEP);
                Console.WriteLine("recv buffer length : {0}, {1}", receivedSize, receiveBuffer.Length);

                // 현재 PC에 출력할 output buffer provider의 sample을 추가
                bwp2.AddSamples(receiveBuffer, 0, receivedSize);                
            }
        }

        WaveOut waveOut = new WaveOut(WaveCallbackInfo.FunctionCallback());


        //전화 왔을 때의 벨소리 처리 부분
        private void PlayMP3()
        {            
            using (var ms = File.OpenRead("HigherPlace.mp3"))
            using (var rdr = new Mp3FileReader(ms))
            using (var wavStream = WaveFormatConversionStream.CreatePcmStream(rdr))
            using (var baStream = new BlockAlignReductionStream(wavStream))
            {
                //설정해 놓은 mp3 음성파일을 PC에 재생
                waveOut.Init(baStream);
                waveOut.Play();
                while (waveOut.PlaybackState == PlaybackState.Playing)
                {
                    Thread.Sleep(100);
                }
            }
        }




        private void AnimateWindow()
        {
            ResetStartPosition(); // 팝업창의 설정
            Animate.AnimateWindow(Handle, DeleayTime, Flag); // WinApi 호출
        }

        private void HideAnimateWindow() // 사라지는 애니메이션
        {
            Animate.AnimateWindow(Handle, DeleayTime, Flag | Animate.DwFlagHide); // WinApi 호출
        }

        private void ResetStartPosition()
        {
            TopMost = true; // 창을 맨 앞에 나오게 하기 위해
            StartPosition = FormStartPosition.Manual; // 스타트 포지션을 수동으로 설정
            Location = GetLocation(); // 창이 나타날 위치 설정
        }

        private Point GetLocation() // 나타날 위치 설정을 위한 계산함수
        {
            Rectangle wArea = Screen.GetWorkingArea(this); // 작업표시줄을 제외한 1번 모니터의 작업영역을 가져오는 부분
            var width = wArea.Width - MarginX - Size.Width; // 작업영역의 넓이 에서 여백을 빼고 팝업창의 넓이를 빼는 부분
            var height = wArea.Height - MarginY - Size.Height; // 작업영역 높이 에서 여백을 뺴고 팝업창의 높이를 빼는 부분
            return new Point(width, height); // Point 로 전달
        }


        // 전화 종료 처리
        private void button2_Click(object sender, EventArgs e)
        {
            //마이크로부터 입력받던 Audio data의 recording을 끝냄
            waveOut.Pause();
            waveOut.Dispose();

            udpSocket.SendTo(Encoding.UTF8.GetBytes("RECOGNITION_CALL_REJECT_FROM_SERVER"), remoteEP);
            Console.WriteLine("Send Refuse Mesg");
            HideAnimateWindow();
        }

    }


    public class Animate
    {
        [DllImport("User32.dll", EntryPoint = "AnimateWindow")]
        public static extern bool AnimateWindow(IntPtr hwnd, int dwTime, int dwFlags);

        public const int DwFlagHorPositive = 0x00000001; // 좌 -> 우
        public const int DwFlagHorNegative = 0x00000002; // 우 -> 좌
        public const int DwFlagVerPositive = 0x00000004; // 뒤 -> 아래
        public const int DwFlagVerNegative = 0x00000008; // 아래 -> 위
        public const int DwFlagCenter = 0x00000010; // 가운데부터
        public const int DwFlagHide = 0x00010000; // 숨기기
        public const int DwFlagActivate = 0x00020000; // 활성화(사용안함)
        public const int DwFlagSlide = 0x00040000; // 슬라이드
        public const int DwFlagBlend = 0x00080000; // Fading 효과
    }
}
